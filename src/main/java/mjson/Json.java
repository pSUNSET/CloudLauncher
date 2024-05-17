package mjson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.regex.Pattern;

public class Json implements Serializable {
    private static final long serialVersionUID = 1L;

    static String fetchContent(URL url) {
        java.io.Reader reader = null;
        try {
            reader = new InputStreamReader((InputStream)url.getContent());
            StringBuilder content = new StringBuilder();
            char[] buf = new char[1024];
            int n;
            for (n = reader.read(buf); n > -1; n = reader.read(buf))
                content.append(buf, 0, n);
            return content.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Throwable throwable) {}
        }
    }

    static Json resolvePointer(String pointerRepresentation, Json top) {
        String[] parts = pointerRepresentation.split("/");
        Json result = top;
        for (String p : parts) {
            if (p.length() != 0) {
                p = p.replace("~1", "/").replace("~0", "~");
                if (result.isArray()) {
                    result = result.at(Integer.parseInt(p));
                } else if (result.isObject()) {
                    result = result.at(p);
                } else {
                    throw new RuntimeException("Can't resolve pointer " + pointerRepresentation + " on document " + top
                            .toString(200));
                }
            }
        }
        return result;
    }

    static URI makeAbsolute(URI base, String ref) throws Exception {
        URI refuri;
        if (base != null && base.getAuthority() != null && !(new URI(ref)).isAbsolute()) {
            StringBuilder sb = new StringBuilder();
            if (base.getScheme() != null)
                sb.append(base.getScheme()).append("://");
            sb.append(base.getAuthority());
            if (!ref.startsWith("/"))
                if (ref.startsWith("#")) {
                    sb.append(base.getPath());
                } else {
                    int slashIdx = base.getPath().lastIndexOf('/');
                    sb.append((slashIdx == -1) ? base.getPath() : base.getPath().substring(0, slashIdx)).append("/");
                }
            refuri = new URI(sb.append(ref).toString());
        } else if (base != null) {
            refuri = base.resolve(ref);
        } else {
            refuri = new URI(ref);
        }
        return refuri;
    }

    static Json resolveRef(URI base, Json refdoc, URI refuri, Map<String, Json> resolved, Map<Json, Json> expanded, Function<URI, Json> uriResolver) throws Exception {
        if (refuri.isAbsolute() && (base == null ||
                !base.isAbsolute() ||
                !base.getScheme().equals(refuri.getScheme()) ||
                !Objects.equals(base.getHost(), refuri.getHost()) || base
                .getPort() != refuri.getPort() ||
                !base.getPath().equals(refuri.getPath()))) {
            URI docuri = null;
            refuri = refuri.normalize();
            if (refuri.getHost() == null) {
                docuri = new URI(refuri.getScheme() + ":" + refuri.getPath());
            } else {
                docuri = new URI(refuri.getScheme() + "://" + refuri.getHost() + ((refuri.getPort() > -1) ? (":" + refuri.getPort()) : "") + refuri.getPath());
            }
            refdoc = uriResolver.apply(docuri);
            refdoc = expandReferences(refdoc, refdoc, docuri, resolved, expanded, uriResolver);
        }
        if (refuri.getFragment() == null)
            return refdoc;
        return resolvePointer(refuri.getFragment(), refdoc);
    }

    static Json expandReferences(Json json, Json topdoc, URI base, Map<String, Json> resolved, Map<Json, Json> expanded, Function<URI, Json> uriResolver) throws Exception {
        if (expanded.containsKey(json))
            return json;
        if (json.isObject()) {
            if (json.has("id") && json.at("id").isString())
                base = base.resolve(json.at("id").asString());
            if (json.has("$ref")) {
                URI refuri = makeAbsolute(base, json.at("$ref").asString());
                Json ref = resolved.get(refuri.toString());
                if (ref == null) {
                    ref = object();
                    resolved.put(refuri.toString(), ref);
                    ref.with(resolveRef(base, topdoc, refuri, resolved, expanded, uriResolver), new Object[0]);
                }
                json = ref;
            } else {
                for (Map.Entry<String, Json> e : json.asJsonMap().entrySet())
                    json.set(e.getKey(), expandReferences(e.getValue(), topdoc, base, resolved, expanded, uriResolver));
            }
        } else if (json.isArray()) {
            for (int i = 0; i < json.asJsonList().size(); i++)
                json.set(i,
                        expandReferences(json.at(i), topdoc, base, resolved, expanded, uriResolver));
        }
        expanded.put(json, json);
        return json;
    }

    public static interface Factory {
        Json nil();

        Json bool(boolean param1Boolean);

        Json string(String param1String);

        Json number(Number param1Number);

        Json object();

        Json array();

        Json make(Object param1Object);
    }

    public static interface Function<T, R> {
        R apply(T param1T);
    }

    public static interface Schema {
        Json validate(Json param1Json);

        Json toJson();
    }

    static class DefaultSchema implements Schema {
        static Json maybeError(Json errors, Json E) {
            return (E == null) ? errors : ((errors == null) ? Json.array() : errors).with(E, new Json[0]);
        }

        static Instruction any = new Instruction() {
            public Json apply(Json param) {
                return null;
            }
        };

        class IsObject implements Instruction {
            public Json apply(Json param) {
                return param.isObject() ? null : Json.make(param.toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class IsArray implements Instruction {
            public Json apply(Json param) {
                return param.isArray() ? null : Json.make(param.toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class IsString implements Instruction {
            public Json apply(Json param) {
                return param.isString() ? null : Json.make(param.toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class IsBoolean implements Instruction {
            public Json apply(Json param) {
                return param.isBoolean() ? null : Json.make(param.toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class IsNull implements Instruction {
            public Json apply(Json param) {
                return param.isNull() ? null : Json.make(param.toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class IsNumber implements Instruction {
            public Json apply(Json param) {
                return param.isNumber() ? null : Json.make(param.toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class IsInteger implements Instruction {
            public Json apply(Json param) {
                return (param.isNumber() && (Number)param.getValue() instanceof Integer) ? null : Json.make(param.toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class CheckString implements Instruction {
            int min = 0;

            int max = Integer.MAX_VALUE;

            Pattern pattern;

            public Json apply(Json param) {
                Json errors = null;
                if (!param.isString())
                    return errors;
                String s = param.asString();
                int size = s.codePointCount(0, s.length());
                if (size < this.min || size > this.max)
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("String  " + param.toString(Json.DefaultSchema.this.maxchars) + " has length outside of the permitted range [" + this.min + "," + this.max + "]."));
                if (this.pattern != null && !this.pattern.matcher(s).matches())
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("String  " + param.toString(Json.DefaultSchema.this.maxchars) + " does not match regex " + this.pattern
                            .toString()));
                return errors;
            }
        }

        class CheckNumber implements Instruction {
            double min = Double.NaN;

            double max = Double.NaN;

            double multipleOf = Double.NaN;

            boolean exclusiveMin = false;

            boolean exclusiveMax = false;

            public Json apply(Json param) {
                Json errors = null;
                if (!param.isNumber())
                    return errors;
                double value = param.asDouble();
                if (!Double.isNaN(this.min) && (value < this.min || (this.exclusiveMin && value == this.min)))
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("Number " + param + " is below allowed minimum " + this.min));
                if (!Double.isNaN(this.max) && (value > this.max || (this.exclusiveMax && value == this.max)))
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("Number " + param + " is above allowed maximum " + this.max));
                if (!Double.isNaN(this.multipleOf) && value / this.multipleOf % 1.0D != 0.0D)
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("Number " + param + " is not a multiple of  " + this.multipleOf));
                return errors;
            }
        }

        class CheckArray implements Instruction {
            int min = 0;

            int max = Integer.MAX_VALUE;

            Boolean uniqueitems = null;

            Json.DefaultSchema.Instruction additionalSchema = Json.DefaultSchema.any;

            Json.DefaultSchema.Instruction schema;

            ArrayList<Json.DefaultSchema.Instruction> schemas;

            public Json apply(Json param) {
                Json errors = null;
                if (!param.isArray())
                    return errors;
                if (this.schema == null && this.schemas == null && this.additionalSchema == null)
                    return errors;
                int size = param.asJsonList().size();
                for (int i = 0; i < size; i++) {
                    Json.DefaultSchema.Instruction S = (this.schema != null) ? this.schema : ((this.schemas != null && i < this.schemas.size()) ? this.schemas.get(i) : this.additionalSchema);
                    if (S == null) {
                        errors = Json.DefaultSchema.maybeError(errors, Json.make("Additional items are not permitted: " + param
                                .at(i) + " in " + param.toString(Json.DefaultSchema.this.maxchars)));
                    } else {
                        errors = Json.DefaultSchema.maybeError(errors, S.apply(param.at(i)));
                    }
                    if (this.uniqueitems != null && this.uniqueitems.booleanValue() && param.asJsonList().lastIndexOf(param.at(i)) > i)
                        errors = Json.DefaultSchema.maybeError(errors, Json.make("Element " + param.at(i) + " is duplicate in array."));
                    if (errors != null && !errors.asJsonList().isEmpty())
                        break;
                }
                if (size < this.min || size > this.max)
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("Array  " + param.toString(Json.DefaultSchema.this.maxchars) + " has number of elements outside of the permitted range [" + this.min + "," + this.max + "]."));
                return errors;
            }
        }

        class CheckPropertyPresent implements Instruction {
            String propname;

            public CheckPropertyPresent(String propname) {
                this.propname = propname;
            }

            public Json apply(Json param) {
                if (!param.isObject())
                    return null;
                if (param.has(this.propname))
                    return null;
                return Json.array().add(Json.make("Required property " + this.propname + " missing from object " + param
                        .toString(Json.DefaultSchema.this.maxchars)));
            }
        }

        class CheckObject implements Instruction {
            int min = 0;

            int max = Integer.MAX_VALUE;

            Json.DefaultSchema.Instruction additionalSchema = Json.DefaultSchema.any;

            ArrayList<CheckProperty> props = new ArrayList<>();

            ArrayList<CheckPatternProperty> patternProps = new ArrayList<>();

            static class CheckProperty implements Json.DefaultSchema.Instruction {
                String name;

                Json.DefaultSchema.Instruction schema;

                public CheckProperty(String name, Json.DefaultSchema.Instruction schema) {
                    this.name = name;
                    this.schema = schema;
                }

                public Json apply(Json param) {
                    Json value = param.at(this.name);
                    if (value == null)
                        return null;
                    return this.schema.apply(param.at(this.name));
                }
            }

            static class CheckPatternProperty {
                Pattern pattern;

                Json.DefaultSchema.Instruction schema;

                public CheckPatternProperty(String pattern, Json.DefaultSchema.Instruction schema) {
                    this.pattern = Pattern.compile(pattern);
                    this.schema = schema;
                }

                public Json apply(Json param, Set<String> found) {
                    Json errors = null;
                    for (Map.Entry<String, Json> e : param.asJsonMap().entrySet()) {
                        if (this.pattern.matcher(e.getKey()).find()) {
                            found.add(e.getKey());
                            errors = Json.DefaultSchema.maybeError(errors, this.schema.apply(e.getValue()));
                        }
                    }
                    return errors;
                }
            }

            public Json apply(Json param) {
                Json errors = null;
                if (!param.isObject())
                    return errors;
                HashSet<String> checked = new HashSet<>();
                for (CheckProperty I : this.props) {
                    if (param.has(I.name))
                        checked.add(I.name);
                    errors = Json.DefaultSchema.maybeError(errors, I.apply(param));
                }
                for (CheckPatternProperty I : this.patternProps)
                    errors = Json.DefaultSchema.maybeError(errors, I.apply(param, checked));
                if (this.additionalSchema != Json.DefaultSchema.any)
                    for (Map.Entry<String, Json> e : param.asJsonMap().entrySet()) {
                        if (!checked.contains(e.getKey()))
                            errors = Json.DefaultSchema.maybeError(errors, (this.additionalSchema == null) ?
                                    Json.make("Extra property '" + (String)e.getKey() + "', schema doesn't allow any properties not explicitly defined:" + param

                                            .toString(Json.DefaultSchema.this.maxchars)) : this.additionalSchema
                                    .apply(e.getValue()));
                    }
                if (param.asJsonMap().size() < this.min)
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("Object " + param.toString(Json.DefaultSchema.this.maxchars) + " has fewer than the permitted " + this.min + "  number of properties."));
                if (param.asJsonMap().size() > this.max)
                    errors = Json.DefaultSchema.maybeError(errors, Json.make("Object " + param.toString(Json.DefaultSchema.this.maxchars) + " has more than the permitted " + this.min + "  number of properties."));
                return errors;
            }
        }

        class CheckProperty implements Instruction {
            String name;

            Json.DefaultSchema.Instruction schema;

            public CheckProperty(String name, Json.DefaultSchema.Instruction schema) {
                this.name = name;
                this.schema = schema;
            }

            public Json apply(Json param) {
                Json value = param.at(this.name);
                if (value == null)
                    return null;
                return this.schema.apply(param.at(this.name));
            }
        }

        class CheckPatternProperty {
            Pattern pattern;

            Json.DefaultSchema.Instruction schema;

            public CheckPatternProperty(String pattern, Json.DefaultSchema.Instruction schema) {
                this.pattern = Pattern.compile(pattern);
                this.schema = schema;
            }

            public Json apply(Json param, Set<String> found) {
                Json errors = null;
                for (Map.Entry<String, Json> e : param.asJsonMap().entrySet()) {
                    if (this.pattern.matcher(e.getKey()).find()) {
                        found.add(e.getKey());
                        errors = Json.DefaultSchema.maybeError(errors, this.schema.apply(e.getValue()));
                    }
                }
                return errors;
            }
        }

        class Sequence implements Instruction {
            ArrayList<Json.DefaultSchema.Instruction> seq = new ArrayList<>();

            public Json apply(Json param) {
                Json errors = null;
                for (Json.DefaultSchema.Instruction I : this.seq)
                    errors = Json.DefaultSchema.maybeError(errors, I.apply(param));
                return errors;
            }

            public Sequence add(Json.DefaultSchema.Instruction I) {
                this.seq.add(I);
                return this;
            }
        }

        class CheckType implements Instruction {
            Json types;

            public CheckType(Json types) {
                this.types = types;
            }

            public Json apply(Json param) {
                String ptype = param.isString() ? "string" : (param.isObject() ? "object" : (param.isArray() ? "array" : (param.isNumber() ? "number" : (param.isNull() ? "null" : "boolean"))));
                for (Json type : this.types.asJsonList()) {
                    if (type.asString().equals(ptype))
                        return null;
                    if (type.asString().equals("integer") && param
                            .isNumber() && param
                            .asDouble() % 1.0D == 0.0D)
                        return null;
                }
                return Json.array().add(Json.make("Type mistmatch for " + param.toString(Json.DefaultSchema.this.maxchars) + ", allowed types: " + this.types));
            }
        }

        class CheckEnum implements Instruction {
            Json theenum;

            public CheckEnum(Json theenum) {
                this.theenum = theenum;
            }

            public Json apply(Json param) {
                for (Json option : this.theenum.asJsonList()) {
                    if (param.equals(option))
                        return null;
                }
                return Json.array().add("Element " + param.toString(Json.DefaultSchema.this.maxchars) + " doesn't match any of enumerated possibilities " + this.theenum);
            }
        }

        class CheckAny implements Instruction {
            ArrayList<Json.DefaultSchema.Instruction> alternates = new ArrayList<>();

            Json schema;

            public Json apply(Json param) {
                for (Json.DefaultSchema.Instruction I : this.alternates) {
                    if (I.apply(param) == null)
                        return null;
                }
                return Json.array().add("Element " + param.toString(Json.DefaultSchema.this.maxchars) + " must conform to at least one of available sub-schemas " + this.schema

                        .toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class CheckOne implements Instruction {
            ArrayList<Json.DefaultSchema.Instruction> alternates = new ArrayList<>();

            Json schema;

            public Json apply(Json param) {
                int matches = 0;
                Json errors = Json.array();
                for (Json.DefaultSchema.Instruction I : this.alternates) {
                    Json result = I.apply(param);
                    if (result == null) {
                        matches++;
                        continue;
                    }
                    errors.add(result);
                }
                if (matches != 1)
                    return Json.array().add("Element " + param.toString(Json.DefaultSchema.this.maxchars) + " must conform to exactly one of available sub-schemas, but not more " + this.schema.toString(Json.DefaultSchema.this.maxchars)).add(errors);
                return null;
            }
        }

        class CheckNot implements Instruction {
            Json.DefaultSchema.Instruction I;

            Json schema;

            public CheckNot(Json.DefaultSchema.Instruction I, Json schema) {
                this.I = I;
                this.schema = schema;
            }

            public Json apply(Json param) {
                if (this.I.apply(param) != null)
                    return null;
                return Json.array().add("Element " + param.toString(Json.DefaultSchema.this.maxchars) + " must NOT conform to the schema " + this.schema
                        .toString(Json.DefaultSchema.this.maxchars));
            }
        }

        class CheckSchemaDependency implements Instruction {
            Json.DefaultSchema.Instruction schema;

            String property;

            public CheckSchemaDependency(String property, Json.DefaultSchema.Instruction schema) {
                this.property = property;
                this.schema = schema;
            }

            public Json apply(Json param) {
                if (!param.isObject())
                    return null;
                if (!param.has(this.property))
                    return null;
                return this.schema.apply(param);
            }
        }

        class CheckPropertyDependency implements Instruction {
            Json required;

            String property;

            public CheckPropertyDependency(String property, Json required) {
                this.property = property;
                this.required = required;
            }

            public Json apply(Json param) {
                if (!param.isObject())
                    return null;
                if (!param.has(this.property))
                    return null;
                Json errors = null;
                for (Json p : this.required.asJsonList()) {
                    if (!param.has(p.asString()))
                        errors = Json.DefaultSchema.maybeError(errors, Json.make("Conditionally required property " + p + " missing from object " + param
                                .toString(Json.DefaultSchema.this.maxchars)));
                }
                return errors;
            }
        }

        Instruction compile(Json S, Map<Json, Instruction> compiled) {
            Instruction result = compiled.get(S);
            if (result != null)
                return result;
            Sequence seq = new Sequence();
            compiled.put(S, seq);
            if (S.has("type") && !S.is("type", "any"))
                seq.add(new CheckType(S.at("type").isString() ?
                        Json.array().add(S.at("type")) : S.at("type")));
            if (S.has("enum"))
                seq.add(new CheckEnum(S.at("enum")));
            if (S.has("allOf")) {
                Sequence sub = new Sequence();
                for (Json x : S.at("allOf").asJsonList())
                    sub.add(compile(x, compiled));
                seq.add(sub);
            }
            if (S.has("anyOf")) {
                CheckAny any = new CheckAny();
                any.schema = S.at("anyOf");
                for (Json x : any.schema.asJsonList())
                    any.alternates.add(compile(x, compiled));
                seq.add(any);
            }
            if (S.has("oneOf")) {
                CheckOne any = new CheckOne();
                any.schema = S.at("oneOf");
                for (Json x : any.schema.asJsonList())
                    any.alternates.add(compile(x, compiled));
                seq.add(any);
            }
            if (S.has("not"))
                seq.add(new CheckNot(compile(S.at("not"), compiled), S.at("not")));
            if (S.has("required") && S.at("required").isArray())
                for (Json p : S.at("required").asJsonList())
                    seq.add(new CheckPropertyPresent(p.asString()));
            CheckObject objectCheck = new CheckObject();
            if (S.has("properties"))
                for (Map.Entry<String, Json> p : S.at("properties").asJsonMap().entrySet()) {
                    objectCheck.getClass();
                    objectCheck.props.add(new CheckObject.CheckProperty(p
                            .getKey(), compile(p.getValue(), compiled)));
                }
            if (S.has("patternProperties"))
                for (Map.Entry<String, Json> p : S.at("patternProperties").asJsonMap().entrySet()) {
                    objectCheck.getClass();
                    objectCheck.patternProps.add(new CheckObject.CheckPatternProperty(p.getKey(),
                            compile(p.getValue(), compiled)));
                }
            if (S.has("additionalProperties"))
                if (S.at("additionalProperties").isObject()) {
                    objectCheck.additionalSchema = compile(S.at("additionalProperties"), compiled);
                } else if (!S.at("additionalProperties").asBoolean()) {
                    objectCheck.additionalSchema = null;
                }
            if (S.has("minProperties"))
                objectCheck.min = S.at("minProperties").asInteger();
            if (S.has("maxProperties"))
                objectCheck.max = S.at("maxProperties").asInteger();
            if (!objectCheck.props.isEmpty() || !objectCheck.patternProps.isEmpty() || objectCheck.additionalSchema != DefaultSchema.any || objectCheck.min > 0 || objectCheck.max < Integer.MAX_VALUE)
                seq.add(objectCheck);
            CheckArray arrayCheck = new CheckArray();
            if (S.has("items"))
                if (S.at("items").isObject()) {
                    arrayCheck.schema = compile(S.at("items"), compiled);
                } else {
                    arrayCheck.schemas = new ArrayList<>();
                    for (Json s : S.at("items").asJsonList())
                        arrayCheck.schemas.add(compile(s, compiled));
                }
            if (S.has("additionalItems"))
                if (S.at("additionalItems").isObject()) {
                    arrayCheck.additionalSchema = compile(S.at("additionalItems"), compiled);
                } else if (!S.at("additionalItems").asBoolean()) {
                    arrayCheck.additionalSchema = null;
                }
            if (S.has("uniqueItems"))
                arrayCheck.uniqueitems = Boolean.valueOf(S.at("uniqueItems").asBoolean());
            if (S.has("minItems"))
                arrayCheck.min = S.at("minItems").asInteger();
            if (S.has("maxItems"))
                arrayCheck.max = S.at("maxItems").asInteger();
            if (arrayCheck.schema != null || arrayCheck.schemas != null || arrayCheck.additionalSchema != DefaultSchema.any || arrayCheck.uniqueitems != null || arrayCheck.max < Integer.MAX_VALUE || arrayCheck.min > 0)
                seq.add(arrayCheck);
            CheckNumber numberCheck = new CheckNumber();
            if (S.has("minimum"))
                numberCheck.min = S.at("minimum").asDouble();
            if (S.has("maximum"))
                numberCheck.max = S.at("maximum").asDouble();
            if (S.has("multipleOf"))
                numberCheck.multipleOf = S.at("multipleOf").asDouble();
            if (S.has("exclusiveMinimum"))
                numberCheck.exclusiveMin = S.at("exclusiveMinimum").asBoolean();
            if (S.has("exclusiveMaximum"))
                numberCheck.exclusiveMax = S.at("exclusiveMaximum").asBoolean();
            if (!Double.isNaN(numberCheck.min) || !Double.isNaN(numberCheck.max) || !Double.isNaN(numberCheck.multipleOf))
                seq.add(numberCheck);
            CheckString stringCheck = new CheckString();
            if (S.has("minLength"))
                stringCheck.min = S.at("minLength").asInteger();
            if (S.has("maxLength"))
                stringCheck.max = S.at("maxLength").asInteger();
            if (S.has("pattern"))
                stringCheck.pattern = Pattern.compile(S.at("pattern").asString());
            if (stringCheck.min > 0 || stringCheck.max < Integer.MAX_VALUE || stringCheck.pattern != null)
                seq.add(stringCheck);
            if (S.has("dependencies"))
                for (Map.Entry<String, Json> e : S.at("dependencies").asJsonMap().entrySet()) {
                    if (((Json)e.getValue()).isObject()) {
                        seq.add(new CheckSchemaDependency(e.getKey(), compile(e.getValue(), compiled)));
                        continue;
                    }
                    if (((Json)e.getValue()).isArray()) {
                        seq.add(new CheckPropertyDependency(e.getKey(), e.getValue()));
                        continue;
                    }
                    seq.add(new CheckPropertyDependency(e.getKey(), Json.array(new Object[] { e.getValue() })));
                }
            result = (seq.seq.size() == 1) ? seq.seq.get(0) : seq;
            compiled.put(S, result);
            return result;
        }

        int maxchars = 50;

        URI uri;

        Json theschema;

        Instruction start;

        DefaultSchema(URI uri, Json theschema, Json.Function<URI, Json> relativeReferenceResolver) {
            try {
                this.uri = (uri == null) ? new URI("") : uri;
                if (relativeReferenceResolver == null)
                    relativeReferenceResolver = new Json.Function<URI, Json>() {
                        public Json apply(URI docuri) {
                            try {
                                return Json.read(Json.fetchContent(docuri.toURL()));
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    };
                this.theschema = theschema.dup();
                this.theschema = Json.expandReferences(this.theschema, this.theschema, this.uri, new HashMap<>(), new IdentityHashMap<>(), relativeReferenceResolver);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            this.start = compile(this.theschema, new IdentityHashMap<>());
        }

        public Json validate(Json document) {
            Json result = Json.object(new Object[] { "ok", Boolean.valueOf(true) });
            Json errors = this.start.apply(document);
            return (errors == null) ? result : result.set("errors", errors).set("ok", Boolean.valueOf(false));
        }

        public Json toJson() {
            return this.theschema;
        }

        public Json generate(Json options) {
            return Json.nil();
        }

        static interface Instruction extends Json.Function<Json, Json> {}
    }

    public static Schema schema(Json S) {
        return new DefaultSchema(null, S, null);
    }

    public static Schema schema(URI uri) {
        return schema(uri, (Function<URI, Json>)null);
    }

    public static Schema schema(URI uri, Function<URI, Json> relativeReferenceResolver) {
        try {
            return new DefaultSchema(uri, read(fetchContent(uri.toURL())), relativeReferenceResolver);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Schema schema(Json S, URI uri) {
        return new DefaultSchema(uri, S, null);
    }

    public static class DefaultFactory implements Factory {
        public Json nil() {
            return Json.topnull;
        }

        public Json bool(boolean x) {
            return new Json.BooleanJson(x ? Boolean.TRUE : Boolean.FALSE, null);
        }

        public Json string(String x) {
            return new Json.StringJson(x, null);
        }

        public Json number(Number x) {
            return new Json.NumberJson(x, null);
        }

        public Json array() {
            return new Json.ArrayJson();
        }

        public Json object() {
            return new Json.ObjectJson();
        }

        public Json make(Object anything) {
            if (anything == null)
                return Json.topnull;
            if (anything instanceof Json)
                return (Json)anything;
            if (anything instanceof String)
                return Json.factory().string((String)anything);
            if (anything instanceof java.util.Collection) {
                Json L = array();
                for (Object x : ((Collection<?>) anything).toArray())
                    L.add(Json.factory().make(x));
                return L;
            }
            if (anything instanceof Map) {
                Json O = object();
                for (Map.Entry<?, ?> x : (Iterable<Map.Entry<?, ?>>)((Map)anything).entrySet())
                    O.set(x.getKey().toString(), Json.factory().make(x.getValue()));
                return O;
            }
            if (anything instanceof Boolean)
                return Json.factory().bool(((Boolean)anything).booleanValue());
            if (anything instanceof Number)
                return Json.factory().number((Number)anything);
            if (anything.getClass().isArray()) {
                Class<?> comp = anything.getClass().getComponentType();
                if (!comp.isPrimitive())
                    return Json.array((Object[])anything);
                Json A = array();
                if (boolean.class == comp) {
                    for (boolean b : (boolean[])anything)
                        A.add(Boolean.valueOf(b));
                } else if (byte.class == comp) {
                    for (byte b : (byte[])anything)
                        A.add(Byte.valueOf(b));
                } else if (char.class == comp) {
                    for (char b : (char[])anything)
                        A.add(Character.valueOf(b));
                } else if (short.class == comp) {
                    for (short b : (short[])anything)
                        A.add(Short.valueOf(b));
                } else if (int.class == comp) {
                    for (int b : (int[])anything)
                        A.add(Integer.valueOf(b));
                } else if (long.class == comp) {
                    for (long b : (long[])anything)
                        A.add(Long.valueOf(b));
                } else if (float.class == comp) {
                    for (float b : (float[])anything)
                        A.add(Float.valueOf(b));
                } else if (double.class == comp) {
                    for (double b : (double[])anything)
                        A.add(Double.valueOf(b));
                }
                return A;
            }
            throw new IllegalArgumentException("Don't know how to convert to Json : " + anything);
        }
    }

    public static final Factory defaultFactory = new DefaultFactory();

    private static Factory globalFactory = defaultFactory;

    private static ThreadLocal<Factory> threadFactory = new ThreadLocal<>();

    public static Factory factory() {
        Factory f = threadFactory.get();
        return (f != null) ? f : globalFactory;
    }

    public static void setGlobalFactory(Factory factory) {
        globalFactory = factory;
    }

    public static void attachFactory(Factory factory) {
        threadFactory.set(factory);
    }

    public static void detachFactory() {
        threadFactory.remove();
    }

    public static Json read(String jsonAsString) {
        return (Json)(new Reader()).read(jsonAsString);
    }

    public static Json read(URL location) {
        return (Json)(new Reader()).read(fetchContent(location));
    }

    public static Json read(CharacterIterator it) {
        return (Json)(new Reader()).read(it);
    }

    public static Json nil() {
        return factory().nil();
    }

    public static Json object() {
        return factory().object();
    }

    public static Json object(Object... args) {
        Json j = object();
        if (args.length % 2 != 0)
            throw new IllegalArgumentException("An even number of arguments is expected.");
        for (int i = 0; i < args.length; i++)
            j.set(args[i].toString(), factory().make(args[++i]));
        return j;
    }

    public static Json array() {
        return factory().array();
    }

    public static Json array(Object... args) {
        Json A = array();
        for (Object x : args)
            A.add(factory().make(x));
        return A;
    }

    public static class help {
        public static String escape(String string) {
            return Json.escaper.escapeJsonString(string);
        }

        public static Json resolvePointer(String pointer, Json element) {
            return Json.resolvePointer(pointer, element);
        }
    }

    public static Json make(Object anything) {
        return factory().make(anything);
    }

    Json enclosing = null;

    protected Json() {}

    protected Json(Json enclosing) {
        this.enclosing = enclosing;
    }

    public String toString(int maxCharacters) {
        return toString();
    }

    public void attachTo(Json enclosing) {
        this.enclosing = enclosing;
    }

    public final Json up() {
        return this.enclosing;
    }

    public Json dup() {
        return this;
    }

    public Json at(int index) {
        throw new UnsupportedOperationException();
    }

    public Json at(String property) {
        throw new UnsupportedOperationException();
    }

    public final Json at(String property, Json def) {
        Json x = at(property);
        if (x == null)
            return def;
        return x;
    }

    public final Json at(String property, Object def) {
        return at(property, make(def));
    }

    public boolean has(String property) {
        throw new UnsupportedOperationException();
    }

    public boolean is(String property, Object value) {
        throw new UnsupportedOperationException();
    }

    public boolean is(int index, Object value) {
        throw new UnsupportedOperationException();
    }

    public Json add(Json el) {
        throw new UnsupportedOperationException();
    }

    public final Json add(Object anything) {
        return add(make(anything));
    }

    public Json atDel(String property) {
        throw new UnsupportedOperationException();
    }

    public Json atDel(int index) {
        throw new UnsupportedOperationException();
    }

    public Json delAt(String property) {
        throw new UnsupportedOperationException();
    }

    public Json delAt(int index) {
        throw new UnsupportedOperationException();
    }

    public Json remove(Json el) {
        throw new UnsupportedOperationException();
    }

    public final Json remove(Object anything) {
        return remove(make(anything));
    }

    public Json set(String property, Json value) {
        throw new UnsupportedOperationException();
    }

    public final Json set(String property, Object value) {
        return set(property, make(value));
    }

    public Json set(int index, Object value) {
        throw new UnsupportedOperationException();
    }

    public Json with(Json object, Json[] options) {
        throw new UnsupportedOperationException();
    }

    public Json with(Json object, Object... options) {
        Json[] jopts = new Json[options.length];
        for (int i = 0; i < jopts.length; i++)
            jopts[i] = make(options[i]);
        return with(object, jopts);
    }

    public Object getValue() {
        throw new UnsupportedOperationException();
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    public String asString() {
        throw new UnsupportedOperationException();
    }

    public int asInteger() {
        throw new UnsupportedOperationException();
    }

    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    public long asLong() {
        throw new UnsupportedOperationException();
    }

    public short asShort() {
        throw new UnsupportedOperationException();
    }

    public byte asByte() {
        throw new UnsupportedOperationException();
    }

    public char asChar() {
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> asMap() {
        throw new UnsupportedOperationException();
    }

    public Map<String, Json> asJsonMap() {
        throw new UnsupportedOperationException();
    }

    public List<Object> asList() {
        throw new UnsupportedOperationException();
    }

    public List<Json> asJsonList() {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isPrimitive() {
        return (isString() || isNumber() || isBoolean());
    }

    public String pad(String callback) {
        return (callback != null && callback.length() > 0) ? (callback + "(" +
                toString() + ");") :
                toString();
    }

    protected Json collectWithOptions(Json... options) {
        Json result = object();
        for (Json opt : options) {
            if (opt.isString()) {
                if (!result.has(""))
                    result.set("", object());
                result.at("").set(opt.asString(), Boolean.valueOf(true));
            } else {
                if (!opt.has("for"))
                    opt.set("for", array(new Object[] { "" }));
                Json forPaths = opt.at("for");
                if (!forPaths.isArray())
                    forPaths = array(new Object[] { forPaths });
                for (Json path : forPaths.asJsonList()) {
                    if (!result.has(path.asString()))
                        result.set(path.asString(), object());
                    Json at_path = result.at(path.asString());
                    at_path.set("merge", Boolean.valueOf(opt.is("merge", Boolean.valueOf(true))));
                    at_path.set("dup", Boolean.valueOf(opt.is("dup", Boolean.valueOf(true))));
                    at_path.set("sort", Boolean.valueOf(opt.is("sort", Boolean.valueOf(true))));
                    at_path.set("compareBy", opt.at("compareBy", nil()));
                }
            }
        }
        return result;
    }

    static class NullJson extends Json {
        private static final long serialVersionUID = 1L;

        NullJson() {}

        NullJson(Json e) {
            super(e);
        }

        public Object getValue() {
            return null;
        }

        public Json dup() {
            return new NullJson();
        }

        public boolean isNull() {
            return true;
        }

        public String toString() {
            return "null";
        }

        public List<Object> asList() {
            return Collections.singletonList(null);
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object x) {
            return x instanceof NullJson;
        }
    }

    static NullJson topnull = new NullJson();

    static void setParent(Json el, Json parent) {
        if (el.enclosing == null) {
            el.enclosing = parent;
        } else if (el.enclosing instanceof ParentArrayJson) {
            ((ParentArrayJson)el.enclosing).L.add(parent);
        } else {
            ParentArrayJson A = new ParentArrayJson();
            A.L.add(el.enclosing);
            A.L.add(parent);
            el.enclosing = A;
        }
    }

    static void removeParent(Json el, Json parent) {
        if (el.enclosing == parent) {
            el.enclosing = null;
        } else if (el.enclosing.isArray()) {
            ArrayJson A = (ArrayJson)el.enclosing;
            int idx = 0;
            for (; A.L.get(idx) != parent && idx < A.L.size(); idx++);
            if (idx < A.L.size())
                A.L.remove(idx);
        }
    }

    static class BooleanJson extends Json {
        private static final long serialVersionUID = 1L;

        boolean val;

        BooleanJson() {}

        BooleanJson(Json e) {
            super(e);
        }

        BooleanJson(Boolean val, Json e) {
            super(e);
            this.val = val.booleanValue();
        }

        public Object getValue() {
            return Boolean.valueOf(this.val);
        }

        public Json dup() {
            return new BooleanJson(Boolean.valueOf(this.val), null);
        }

        public boolean asBoolean() {
            return this.val;
        }

        public boolean isBoolean() {
            return true;
        }

        public String toString() {
            return this.val ? "true" : "false";
        }

        public List<Object> asList() {
            return Collections.singletonList(Boolean.valueOf(this.val));
        }

        public int hashCode() {
            return this.val ? 1 : 0;
        }

        public boolean equals(Object x) {
            return (x instanceof BooleanJson && ((BooleanJson)x).val == this.val);
        }
    }

    static class StringJson extends Json {
        private static final long serialVersionUID = 1L;

        String val;

        StringJson() {}

        StringJson(Json e) {
            super(e);
        }

        StringJson(String val, Json e) {
            super(e);
            this.val = val;
        }

        public Json dup() {
            return new StringJson(this.val, null);
        }

        public boolean isString() {
            return true;
        }

        public Object getValue() {
            return this.val;
        }

        public String asString() {
            return this.val;
        }

        public int asInteger() {
            return Integer.parseInt(this.val);
        }

        public float asFloat() {
            return Float.parseFloat(this.val);
        }

        public double asDouble() {
            return Double.parseDouble(this.val);
        }

        public long asLong() {
            return Long.parseLong(this.val);
        }

        public short asShort() {
            return Short.parseShort(this.val);
        }

        public byte asByte() {
            return Byte.parseByte(this.val);
        }

        public char asChar() {
            return this.val.charAt(0);
        }

        public List<Object> asList() {
            return Collections.singletonList(this.val);
        }

        public String toString() {
            return '"' + escaper.escapeJsonString(this.val) + '"';
        }

        public String toString(int maxCharacters) {
            if (this.val.length() <= maxCharacters)
                return toString();
            return '"' + escaper.escapeJsonString(this.val.subSequence(0, maxCharacters)) + "...\"";
        }

        public int hashCode() {
            return this.val.hashCode();
        }

        public boolean equals(Object x) {
            return (x instanceof StringJson && ((StringJson)x).val.equals(this.val));
        }
    }

    static class NumberJson extends Json {
        private static final long serialVersionUID = 1L;

        Number val;

        NumberJson() {}

        NumberJson(Json e) {
            super(e);
        }

        NumberJson(Number val, Json e) {
            super(e);
            this.val = val;
        }

        public Json dup() {
            return new NumberJson(this.val, null);
        }

        public boolean isNumber() {
            return true;
        }

        public Object getValue() {
            return this.val;
        }

        public String asString() {
            return this.val.toString();
        }

        public int asInteger() {
            return this.val.intValue();
        }

        public float asFloat() {
            return this.val.floatValue();
        }

        public double asDouble() {
            return this.val.doubleValue();
        }

        public long asLong() {
            return this.val.longValue();
        }

        public short asShort() {
            return this.val.shortValue();
        }

        public byte asByte() {
            return this.val.byteValue();
        }

        public List<Object> asList() {
            return Collections.singletonList(this.val);
        }

        public String toString() {
            return this.val.toString();
        }

        public int hashCode() {
            return this.val.hashCode();
        }

        public boolean equals(Object x) {
            return (x instanceof NumberJson && this.val.doubleValue() == ((NumberJson)x).val.doubleValue());
        }
    }

    static class ArrayJson extends Json {
        private static final long serialVersionUID = 1L;

        List<Json> L = new ArrayList<>();

        ArrayJson() {}

        ArrayJson(Json e) {
            super(e);
        }

        public Json dup() {
            ArrayJson j = new ArrayJson();
            for (Json e : this.L) {
                Json v = e.dup();
                v.enclosing = j;
                j.L.add(v);
            }
            return j;
        }

        public Json set(int index, Object value) {
            Json jvalue = make(value);
            this.L.set(index, jvalue);
            setParent(jvalue, this);
            return this;
        }

        public List<Json> asJsonList() {
            return this.L;
        }

        public List<Object> asList() {
            ArrayList<Object> A = new ArrayList();
            for (Json x : this.L)
                A.add(x.getValue());
            return A;
        }

        public boolean is(int index, Object value) {
            if (index < 0 || index >= this.L.size())
                return false;
            return ((Json)this.L.get(index)).equals(make(value));
        }

        public Object getValue() {
            return asList();
        }

        public boolean isArray() {
            return true;
        }

        public Json at(int index) {
            return this.L.get(index);
        }

        public Json add(Json el) {
            this.L.add(el);
            setParent(el, this);
            return this;
        }

        public Json remove(Json el) {
            this.L.remove(el);
            el.enclosing = null;
            return this;
        }

        boolean isEqualJson(Json left, Json right) {
            if (left == null)
                return (right == null);
            return left.equals(right);
        }

        boolean isEqualJson(Json left, Json right, Json fields) {
            if (fields.isNull())
                return left.equals(right);
            if (fields.isString())
                return isEqualJson(resolvePointer(fields.asString(), left),
                        resolvePointer(fields.asString(), right));
            if (fields.isArray()) {
                for (Json field : fields.asJsonList()) {
                    if (!isEqualJson(resolvePointer(field.asString(), left),
                            resolvePointer(field.asString(), right)))
                        return false;
                }
                return true;
            }
            throw new IllegalArgumentException("Compare by options should be either a property name or an array of property names: " + fields);
        }

        int compareJson(Json left, Json right, Json fields) {
            if (fields.isNull())
                return ((Comparable<Object>)left.getValue()).compareTo(right.getValue());
            if (fields.isString()) {
                Json leftProperty = resolvePointer(fields.asString(), left);
                Json rightProperty = resolvePointer(fields.asString(), right);
                return ((Comparable<Json>)leftProperty).compareTo(rightProperty);
            }
            if (fields.isArray()) {
                for (Json field : fields.asJsonList()) {
                    Json leftProperty = resolvePointer(field.asString(), left);
                    Json rightProperty = resolvePointer(field.asString(), right);
                    int result = ((Comparable<Json>)leftProperty).compareTo(rightProperty);
                    if (result != 0)
                        return result;
                }
                return 0;
            }
            throw new IllegalArgumentException("Compare by options should be either a property name or an array of property names: " + fields);
        }

        Json withOptions(Json array, Json allOptions, String path) {
            Json opts = allOptions.at(path, object());
            boolean dup = opts.is("dup", Boolean.valueOf(true));
            Json compareBy = opts.at("compareBy", nil());
            if (opts.is("sort", Boolean.valueOf(true))) {
                int thisIndex = 0, thatIndex = 0;
                while (thatIndex < array.asJsonList().size()) {
                    Json thatElement = array.at(thatIndex);
                    if (thisIndex == this.L.size()) {
                        this.L.add(dup ? thatElement.dup() : thatElement);
                        thisIndex++;
                        thatIndex++;
                        continue;
                    }
                    int compared = compareJson(at(thisIndex), thatElement, compareBy);
                    if (compared < 0) {
                        thisIndex++;
                        continue;
                    }
                    if (compared > 0) {
                        this.L.add(thisIndex, dup ? thatElement.dup() : thatElement);
                        thatIndex++;
                        continue;
                    }
                    thatIndex++;
                }
            } else {
                for (Json thatElement : array.asJsonList()) {
                    boolean present = false;
                    for (Json thisElement : this.L) {
                        if (isEqualJson(thisElement, thatElement, compareBy)) {
                            present = true;
                            break;
                        }
                    }
                    if (!present)
                        this.L.add(dup ? thatElement.dup() : thatElement);
                }
            }
            return this;
        }

        public Json with(Json object, Json... options) {
            if (object == null)
                return this;
            if (!object.isArray()) {
                add(object);
            } else {
                if (options.length > 0) {
                    Json O = collectWithOptions(options);
                    return withOptions(object, O, "");
                }
                this.L.addAll(((ArrayJson)object).L);
            }
            return this;
        }

        public Json atDel(int index) {
            Json el = this.L.remove(index);
            if (el != null)
                el.enclosing = null;
            return el;
        }

        public Json delAt(int index) {
            Json el = this.L.remove(index);
            if (el != null)
                el.enclosing = null;
            return this;
        }

        public String toString() {
            return toString(2147483647);
        }

        public String toString(int maxCharacters) {
            return toStringImpl(maxCharacters, new IdentityHashMap<>());
        }

        String toStringImpl(int maxCharacters, Map<Json, Json> done) {
            StringBuilder sb = new StringBuilder("[");
            for (Iterator<Json> i = this.L.iterator(); i.hasNext(); ) {
                Json value = i.next();
                String s = value.isObject() ? ((Json.ObjectJson)value).toStringImpl(maxCharacters, done) : (value.isArray() ? ((ArrayJson)value).toStringImpl(maxCharacters, done) : value.toString(maxCharacters));
                if (sb.length() + s.length() > maxCharacters) {
                    s = s.substring(0, Math.max(0, maxCharacters - sb.length()));
                } else {
                    sb.append(s);
                }
                if (i.hasNext())
                    sb.append(",");
                if (sb.length() >= maxCharacters) {
                    sb.append("...");
                    break;
                }
            }
            sb.append("]");
            return sb.toString();
        }

        public int hashCode() {
            return this.L.hashCode();
        }

        public boolean equals(Object x) {
            return (x instanceof ArrayJson && ((ArrayJson)x).L.equals(this.L));
        }
    }

    static class ParentArrayJson extends ArrayJson {
        private static final long serialVersionUID = 1L;
    }

    static class ObjectJson extends Json {
        private static final long serialVersionUID = 1L;

        Map<String, Json> object = new HashMap<>();

        ObjectJson() {}

        ObjectJson(Json e) {
            super(e);
        }

        public Json dup() {
            ObjectJson j = new ObjectJson();
            for (Map.Entry<String, Json> e : this.object.entrySet()) {
                Json v = ((Json)e.getValue()).dup();
                v.enclosing = j;
                j.object.put(e.getKey(), v);
            }
            return j;
        }

        public boolean has(String property) {
            return this.object.containsKey(property);
        }

        public boolean is(String property, Object value) {
            Json p = this.object.get(property);
            if (p == null)
                return false;
            return p.equals(make(value));
        }

        public Json at(String property) {
            return this.object.get(property);
        }

        protected Json withOptions(Json other, Json allOptions, String path) {
            if (!allOptions.has(path))
                allOptions.set(path, object());
            Json options = allOptions.at(path, object());
            boolean duplicate = options.is("dup", Boolean.valueOf(true));
            if (options.is("merge", Boolean.valueOf(true))) {
                for (Map.Entry<String, Json> e : other.asJsonMap().entrySet()) {
                    Json local = this.object.get(e.getKey());
                    if (local instanceof ObjectJson) {
                        ((ObjectJson)local).withOptions(e.getValue(), allOptions, path + "/" + (String)e.getKey());
                        continue;
                    }
                    if (local instanceof Json.ArrayJson) {
                        ((Json.ArrayJson)local).withOptions(e.getValue(), allOptions, path + "/" + (String)e.getKey());
                        continue;
                    }
                    set(e.getKey(), duplicate ? ((Json)e.getValue()).dup() : e.getValue());
                }
            } else if (duplicate) {
                for (Map.Entry<String, Json> e : other.asJsonMap().entrySet())
                    set(e.getKey(), ((Json)e.getValue()).dup());
            } else {
                for (Map.Entry<String, Json> e : other.asJsonMap().entrySet())
                    set(e.getKey(), e.getValue());
            }
            return this;
        }

        public Json with(Json x, Json... options) {
            if (x == null)
                return this;
            if (!x.isObject())
                throw new UnsupportedOperationException();
            if (options.length > 0) {
                Json O = collectWithOptions(options);
                return withOptions(x, O, "");
            }
            for (Map.Entry<String, Json> e : x.asJsonMap().entrySet())
                set(e.getKey(), e.getValue());
            return this;
        }

        public Json set(String property, Json el) {
            if (property == null)
                throw new IllegalArgumentException("Null property names are not allowed, value is " + el);
            if (el == null)
                el = nil();
            setParent(el, this);
            this.object.put(property, el);
            return this;
        }

        public Json atDel(String property) {
            Json el = this.object.remove(property);
            removeParent(el, this);
            return el;
        }

        public Json delAt(String property) {
            Json el = this.object.remove(property);
            removeParent(el, this);
            return this;
        }

        public Object getValue() {
            return asMap();
        }

        public boolean isObject() {
            return true;
        }

        public Map<String, Object> asMap() {
            HashMap<String, Object> m = new HashMap<>();
            for (Map.Entry<String, Json> e : this.object.entrySet())
                m.put(e.getKey(), ((Json)e.getValue()).getValue());
            return m;
        }

        public Map<String, Json> asJsonMap() {
            return this.object;
        }

        public String toString() {
            return toString(2147483647);
        }

        public String toString(int maxCharacters) {
            return toStringImpl(maxCharacters, new IdentityHashMap<>());
        }

        String toStringImpl(int maxCharacters, Map<Json, Json> done) {
            StringBuilder sb = new StringBuilder("{");
            if (done.containsKey(this))
                return sb.append("...}").toString();
            done.put(this, this);
            for (Iterator<Map.Entry<String, Json>> i = this.object.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry<String, Json> x = i.next();
                sb.append('"');
                sb.append(escaper.escapeJsonString(x.getKey()));
                sb.append('"');
                sb.append(":");
                String s = ((Json)x.getValue()).isObject() ? ((ObjectJson)x.getValue()).toStringImpl(maxCharacters, done) : (((Json)x.getValue()).isArray() ? ((Json.ArrayJson)x.getValue()).toStringImpl(maxCharacters, done) : ((Json)x.getValue()).toString(maxCharacters));
                if (sb.length() + s.length() > maxCharacters)
                    s = s.substring(0, Math.max(0, maxCharacters - sb.length()));
                sb.append(s);
                if (i.hasNext())
                    sb.append(",");
                if (sb.length() >= maxCharacters) {
                    sb.append("...");
                    break;
                }
            }
            sb.append("}");
            return sb.toString();
        }

        public int hashCode() {
            return this.object.hashCode();
        }

        public boolean equals(Object x) {
            return (x instanceof ObjectJson && ((ObjectJson)x).object.equals(this.object));
        }
    }

    static Escaper escaper = new Escaper(false);

    static final class Escaper {
        private static final char[] HEX_CHARS = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        private static final Set<Character> JS_ESCAPE_CHARS;

        private static final Set<Character> HTML_ESCAPE_CHARS;

        private final boolean escapeHtmlCharacters;

        static {
            Set<Character> mandatoryEscapeSet = new HashSet<>();
            mandatoryEscapeSet.add(Character.valueOf('"'));
            mandatoryEscapeSet.add(Character.valueOf('\\'));
            JS_ESCAPE_CHARS = Collections.unmodifiableSet(mandatoryEscapeSet);
            Set<Character> htmlEscapeSet = new HashSet<>();
            htmlEscapeSet.add(Character.valueOf('<'));
            htmlEscapeSet.add(Character.valueOf('>'));
            htmlEscapeSet.add(Character.valueOf('&'));
            htmlEscapeSet.add(Character.valueOf('='));
            htmlEscapeSet.add(Character.valueOf('\''));
            HTML_ESCAPE_CHARS = Collections.unmodifiableSet(htmlEscapeSet);
        }

        Escaper(boolean escapeHtmlCharacters) {
            this.escapeHtmlCharacters = escapeHtmlCharacters;
        }

        public String escapeJsonString(CharSequence plainText) {
            StringBuilder escapedString = new StringBuilder(plainText.length() + 20);
            try {
                escapeJsonString(plainText, escapedString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return escapedString.toString();
        }

        private void escapeJsonString(CharSequence plainText, StringBuilder out) throws IOException {
            int pos = 0;
            int len = plainText.length();
            int i;
            int charCount;
            for (i = 0; i < len; i += charCount) {
                int codePoint = Character.codePointAt(plainText, i);
                charCount = Character.charCount(codePoint);
                if (isControlCharacter(codePoint) || mustEscapeCharInJsString(codePoint)) {
                    out.append(plainText, pos, i);
                    pos = i + charCount;
                    switch (codePoint) {
                        case 8:
                            out.append("\\b");
                            break;
                        case 9:
                            out.append("\\t");
                            break;
                        case 10:
                            out.append("\\n");
                            break;
                        case 12:
                            out.append("\\f");
                            break;
                        case 13:
                            out.append("\\r");
                            break;
                        case 92:
                            out.append("\\\\");
                            break;
                        case 47:
                            out.append("\\/");
                            break;
                        case 34:
                            out.append("\\\"");
                            break;
                        default:
                            appendHexJavaScriptRepresentation(codePoint, out);
                            break;
                    }
                }
            }
            out.append(plainText, pos, len);
        }

        private boolean mustEscapeCharInJsString(int codepoint) {
            if (!Character.isSupplementaryCodePoint(codepoint)) {
                char c = (char)codepoint;
                return (JS_ESCAPE_CHARS.contains(Character.valueOf(c)) || (this.escapeHtmlCharacters && HTML_ESCAPE_CHARS
                        .contains(Character.valueOf(c))));
            }
            return false;
        }

        private static boolean isControlCharacter(int codePoint) {
            return (codePoint < 32 || codePoint == 8232 || codePoint == 8233 || (codePoint >= 127 && codePoint <= 159));
        }

        private static void appendHexJavaScriptRepresentation(int codePoint, Appendable out) throws IOException {
            if (Character.isSupplementaryCodePoint(codePoint)) {
                char[] surrogates = Character.toChars(codePoint);
                appendHexJavaScriptRepresentation(surrogates[0], out);
                appendHexJavaScriptRepresentation(surrogates[1], out);
                return;
            }
            out.append("\\u")
                    .append(HEX_CHARS[codePoint >>> 12 & 0xF])
                    .append(HEX_CHARS[codePoint >>> 8 & 0xF])
                    .append(HEX_CHARS[codePoint >>> 4 & 0xF])
                    .append(HEX_CHARS[codePoint & 0xF]);
        }
    }

    public static class MalformedJsonException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public MalformedJsonException(String msg) {
            super(msg);
        }
    }

    private static class Reader {
        private static final Object OBJECT_END = new String("}");

        private static final Object ARRAY_END = new String("]");

        private static final Object OBJECT_START = new String("{");

        private static final Object ARRAY_START = new String("[");

        private static final Object COLON = new String(":");

        private static final Object COMMA = new String(",");

        private static final HashSet<Object> PUNCTUATION = new HashSet(
                Arrays.asList(new Object[] { OBJECT_END, OBJECT_START, ARRAY_END, ARRAY_START, COLON, COMMA }));

        public static final int FIRST = 0;

        public static final int CURRENT = 1;

        public static final int NEXT = 2;

        private static Map<Character, Character> escapes = new HashMap<>();

        private CharacterIterator it;

        private char c;

        private Object token;

        static {
            escapes.put(Character.valueOf('"'), Character.valueOf('"'));
            escapes.put(Character.valueOf('\\'), Character.valueOf('\\'));
            escapes.put(Character.valueOf('/'), Character.valueOf('/'));
            escapes.put(Character.valueOf('b'), Character.valueOf('\b'));
            escapes.put(Character.valueOf('f'), Character.valueOf('\f'));
            escapes.put(Character.valueOf('n'), Character.valueOf('\n'));
            escapes.put(Character.valueOf('r'), Character.valueOf('\r'));
            escapes.put(Character.valueOf('t'), Character.valueOf('\t'));
        }

        private StringBuffer buf = new StringBuffer();

        private char next() {
            if (this.it.getIndex() == this.it.getEndIndex())
                throw new Json.MalformedJsonException("Reached end of input at the " + this.it
                        .getIndex() + "th character.");
            this.c = this.it.next();
            return this.c;
        }

        private char previous() {
            this.c = this.it.previous();
            return this.c;
        }

        private void skipWhiteSpace() {
            do {
                if (Character.isWhitespace(this.c))
                    continue;
                if (this.c == '/') {
                    next();
                    if (this.c == '*') {
                        do {

                        } while (this.c != Character.MAX_VALUE && (
                                next() != '*' || next() != '/'));
                        if (this.c == Character.MAX_VALUE)
                            throw new Json.MalformedJsonException("Unterminated comment while parsing JSON string.");
                    } else if (this.c == '/') {
                        while (this.c != '\n' && this.c != Character.MAX_VALUE)
                            next();
                    } else {
                        previous();
                        break;
                    }
                } else {
                    break;
                }
            } while (next() != Character.MAX_VALUE);
        }

        public Object read(CharacterIterator ci, int start) {
            this.it = ci;
            switch (start) {
                case 0:
                    this.c = this.it.first();
                    break;
                case 1:
                    this.c = this.it.current();
                    break;
                case 2:
                    this.c = this.it.next();
                    break;
            }
            return read();
        }

        public Object read(CharacterIterator it) {
            return read(it, 2);
        }

        public Object read(String string) {
            return read(new StringCharacterIterator(string), 0);
        }

        private void expected(Object expectedToken, Object actual) {
            if (expectedToken != actual)
                throw new Json.MalformedJsonException("Expected " + expectedToken + ", but got " + actual + " instead");
        }

        private <T> T read() {
            skipWhiteSpace();
            char ch = this.c;
            next();
            switch (ch) {
                case '"':
                    this.token = readString();
                    return (T)this.token;
                case '[':
                    this.token = readArray();
                    return (T)this.token;
                case ']':
                    this.token = ARRAY_END;
                    return (T)this.token;
                case ',':
                    this.token = COMMA;
                    return (T)this.token;
                case '{':
                    this.token = readObject();
                    return (T)this.token;
                case '}':
                    this.token = OBJECT_END;
                    return (T)this.token;
                case ':':
                    this.token = COLON;
                    return (T)this.token;
                case 't':
                    if (this.c != 'r' || next() != 'u' || next() != 'e')
                        throw new Json.MalformedJsonException("Invalid JSON token: expected 'true' keyword.");
                    next();
                    this.token = Json.factory().bool(Boolean.TRUE.booleanValue());
                    return (T)this.token;
                case 'f':
                    if (this.c != 'a' || next() != 'l' || next() != 's' || next() != 'e')
                        throw new Json.MalformedJsonException("Invalid JSON token: expected 'false' keyword.");
                    next();
                    this.token = Json.factory().bool(Boolean.FALSE.booleanValue());
                    return (T)this.token;
                case 'n':
                    if (this.c != 'u' || next() != 'l' || next() != 'l')
                        throw new Json.MalformedJsonException("Invalid JSON token: expected 'null' keyword.");
                    next();
                    this.token = Json.nil();
                    return (T)this.token;
            }
            this.c = this.it.previous();
            if (Character.isDigit(this.c) || this.c == '-') {
                this.token = readNumber();
            } else {
                throw new Json.MalformedJsonException("Invalid JSON near position: " + this.it.getIndex());
            }
            return (T)this.token;
        }

        private String readObjectKey() {
            Object key = read();
            if (key == null)
                throw new Json.MalformedJsonException("Missing object key (don't forget to put quotes!).");
            if (key == OBJECT_END)
                return null;
            if (PUNCTUATION.contains(key))
                throw new Json.MalformedJsonException("Missing object key, found: " + key);
            return ((Json)key).asString();
        }

        private Json readObject() {
            Json ret = Json.object();
            String key = readObjectKey();
            while (this.token != OBJECT_END) {
                expected(COLON, read());
                if (this.token != OBJECT_END) {
                    Json value = read();
                    ret.set(key, value);
                    if (read() == COMMA) {
                        key = readObjectKey();
                        if (key == null || PUNCTUATION.contains(key))
                            throw new Json.MalformedJsonException("Expected a property name, but found: " + key);
                        continue;
                    }
                    expected(OBJECT_END, this.token);
                }
            }
            return ret;
        }

        private Json readArray() {
            Json ret = Json.array();
            Object value = read();
            while (this.token != ARRAY_END) {
                if (PUNCTUATION.contains(value))
                    throw new Json.MalformedJsonException("Expected array element, but found: " + value);
                ret.add((Json)value);
                if (read() == COMMA) {
                    value = read();
                    if (value == ARRAY_END)
                        throw new Json.MalformedJsonException("Expected array element, but found end of array after command.");
                    continue;
                }
                expected(ARRAY_END, this.token);
            }
            return ret;
        }

        private Json readNumber() {
            int length = 0;
            boolean isFloatingPoint = false;
            this.buf.setLength(0);
            if (this.c == '-')
                add();
            length += addDigits();
            if (this.c == '.') {
                add();
                length += addDigits();
                isFloatingPoint = true;
            }
            if (this.c == 'e' || this.c == 'E') {
                add();
                if (this.c == '+' || this.c == '-')
                    add();
                addDigits();
                isFloatingPoint = true;
            }
            String s = this.buf.toString();
            Number n = isFloatingPoint ? ((length < 17) ? Double.valueOf(s) : new BigDecimal(s)) : ((length < 20) ? Long.valueOf(s) : new BigInteger(s));
            return Json.factory().number(n);
        }

        private int addDigits() {
            int ret;
            for (ret = 0; Character.isDigit(this.c); ret++)
                add();
            return ret;
        }

        private Json readString() {
            this.buf.setLength(0);
            while (this.c != '"') {
                if (this.c == '\\') {
                    next();
                    if (this.c == 'u') {
                        add(unicode());
                        continue;
                    }
                    Object value = escapes.get(Character.valueOf(this.c));
                    if (value != null)
                        add(((Character)value).charValue());
                    continue;
                }
                add();
            }
            next();
            return Json.factory().string(this.buf.toString());
        }

        private void add(char cc) {
            this.buf.append(cc);
            next();
        }

        private void add() {
            add(this.c);
        }

        private char unicode() {
            int value = 0;
            for (int i = 0; i < 4; i++) {
                switch (next()) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        value = (value << 4) + this.c - 48;
                        break;
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                        value = (value << 4) + this.c - 97 + 10;
                        break;
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                        value = (value << 4) + this.c - 65 + 10;
                        break;
                }
            }
            return (char)value;
        }

        private Reader() {}
    }

    public static void main(String[] argv) {
        try {
            URI assetUri = new URI("https://raw.githubusercontent.com/pudo/aleph/master/aleph/schema/entity/asset.json");
            URI schemaRoot = new URI("https://raw.githubusercontent.com/pudo/aleph/master/aleph/schema/");
            schema(assetUri);
            Json asset = read(assetUri.toURL());
            schema(asset, schemaRoot);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
