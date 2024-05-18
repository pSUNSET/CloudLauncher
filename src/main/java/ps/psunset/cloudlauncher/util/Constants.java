package ps.psunset.cloudlauncher.util;

import ps.psunset.cloudlauncher.Launcher;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Constants {
    // With https://www.base64-image.de/
    public static final String MC_LAUNCHER_ICON = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAAAAlC+aJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAACPsSURBVGhDXXpprCznmVbte1f1vvfZt7v72tdb7GQm2IlNZgYBgggC8wMGIZD4MRLDJkaK+IuE4AcSPwAxExTICBIyyaAMyWS1TbzE9vW9Xu5y9rX37tr3Kp6vTxagfG67+5zq73vX532eqqKznBzUzw/yBh9pisY7z/fn44F5crB/9x1vNk7SVDOKhVqbLRSb69uNbk8UJYahWZbDK0XT+C759/8cl7+4fM1pOk+z/HQwvXs8+/h0fm76RV3LqXwwHM9tRy2oPMdmWYbFeJbDeoHnliXh1Vsr20sNlqaTJMFfsRHP0hyLXywO4sBidWI8dshSfIjD6OzoYHa4G45OfXNsmXYUp612Uy8W8zyjGDZMc7bUaG1dLzdaPM8z2A0rXhpK/vfLiBCzL/8Hs8I4eXwy3JtGJxNvbDk0x2OdydT0XK9QkGmGojkxp+g0jrI4yrOMk8QwCDPHfuVm7+mrazzLZGmGrfCGJQ6QreiU2E/2wwdYH+HLSXJ0797R2z9OPFPW1DDNREFgaSZNUq1oRPAgZyqVMv4fUmxp/Wp9eV0SBeI9y2L7SweYhdXkIJFZWB8m9w/7j4b+2WB6cd7POFYs1qKMhlfw0Z1N5/0TUVKMZiejqNA2OZbjJVmQJYR4PBx9bqP+mSe3JIGHlTAeSSdpzylsRHZYbJrHSZxE4fDRg9G9t6k0pgWB43lJEERZFhQF2fcclxME2/X2j88TikXkrZPd4fEBjMA6eUqyt4jHr0rp59bH6YeHFx+fW8dn/fFoLOqGXKrFGRXFSZJmQRAxkibWuuPZ9Hz3E0SKUQqz2XQ2uAg9H1Evlivf2Z2++dFRkmEbCiekKKYMxbCIFNkyz5MkDjxvtPd4vPsJzyEzqVFQkSkWhclx8KRQNOI44Riu12tzbH7RH+QMgzZIPcucjOKEWI/w/HLBhfmoewpJfnQ8fDxyTHOW0VR9eUmvVoMw8oMoiBMvjF0/tG1blNTq0mZK86PDPXzdaPU8P+gf7sGHJI51Xfve7vjBwRlWRCjTFKuSXqXRF2SPNImC8GL34XjvAR96Dx/vClzebtZZBFxWcaBTcbLn+ilDo5WR1rnj1zptxSgxvBREES0q1XoDvZWTQqLoLF3kmGJo5vBi+PbeYGJ7M9sXjLLKC0gCx6AOuJRikAPbixAOeMKqhcCx54NzRZYqvVVEYLz/QFIUo9GF3XFGG3T82y9sdupFhEYSsBvLfvnLX0ag4KI9n/cffihm4fnZ2d7+wdpKxyiVC8i1osqStEiFyPA8bFIKRUU3Kq0WL6uCpJBGShK0BCcpHAejCRRQJC40yzCOF7y3e3LuYH2n1+t1DLWiCr5rrS/hLV8qyBVDMVRJEXlzNOg1qyLPNUuG7zpRkqB0sebo5EhUVZQAwmwltDUzN1pFHmEiGUAPYPs0jeN4PrwoSJwkK4wgXbuyVSgUSOA5dlESdIyUAWsIYjJJmtCADIpOwjB0nTQKWIaWeT70vSiOYTqFPkftL8rp4fHZkR2xcfzs9e31VqlaVKPALRoGoAsWIHVAkzRLBYEvaAqbJ2VdXeo1Nzc3dJZqaMJSu17v9MYnB3EQIiJ8Gt2fRO8+OEX9o3uiKCarIH6+77Oxl2Xp+WBoaCrH0mqhQFqbZhiOA0TiNLiBT3j1HTOJ/MhzIs91ptPU92FsHPqpa6GLFqVPsgB/h9PZvcP+dre1tdw2dCUKI4ZhJ3PL0AvY3g0idDAW9sMYcdEK6mg6Q7uZblAu6aiiwLZ0RdzaWq9WK/ZkBHhNk5jNkjf2ZqhIFCoaD8iaxOhq35VY+vj41Lesi7NzVBdSQSYPQScKEyRA0wV+GPie77q2Nbk4scf9yJ7b03ESBihBkkbHwpbIDzxAtPDm/qP9laVlJJEVeAQ7RdJi0riiJMZZbsNu0uJkNsFbSZR8PxA4xvYD1EZB01zUn+MIIl9rd5Fn35pTLBd59jRM3npwAYuQZKA7kDPoP/r4bH/v7PBQoLMk9ERRRPmiQCzLtGbj6ehiMh6a85ljma5luY7tOi7sjnzHs017No1cB1hojQbm+YljWbAeaRgOx1M3RRfNLQfDbuoG+D0gmBdFdHaQUXaUYBdklrxSNM4hr/Apy7wokmWMNs6zLSpLFVWVJCkNQ9Q66pJOo3cPJyf9KRZk0G72dDI6eHTw4GN/MmKiIMJ5aJf5bDYe2eYsjDDKshQjA0gkikDuNE4t04HfGJZ0no8uzmbnJ4E5RUnOz46m/Ys4IYVxcjGUy83x1OR5LkozL4gFnpvaDsNJYZK5SYqsolnCRR0ha7QgwW2GynVVsYMg5zhRkkECYDeQXDMMhCX2XZoXEDg/9B6dz2EMma/9g93Yc83ZrNusTYHVjg8HAMwwmhUEGoNAwITHDmgBBrUCnsTlrOe6ke8LPIvhnUY+yBLPUL5jz85OvRDdlU4CCv0zGI7QTqOZCZcGM/vR0dnxYPrRwfnRaX98Mfz4sH9/7wzo/vh0cABGNJsOJvMwz72UyuCAqrhBHKDHqFwp6MBA7IXqTOKQycIHA7M/Mdm///f+7u5br9sXpxyV8Tzrp+z6xiadRawoYd5xnEDRrCDLtuMKoG4s63peHMatUh3lz9C5IIq2F2io6ThCnDwPxsfFpVXLDQ5GDlxGznlFu/9gbzj19i5m/ZOzhGIQI9N2Q8sZhdTU9iemNfGigekE44HjR2MvRqGixNEYGNKwCgYgp5gtc9tDUDB01TzOWIGKI2Y2HPjT0bAP7D88ODy59uRT9V4XDYd4L2YcOGCKGQBnwjBAw5PyDcNirUJFeRyHSRSBWkVR6DtOlsQgRYFjonBPRhbLS64XTYPsx+8/POrP+jNiEwgA/MRXUE68wIlsLnKswND4ETlwUAEuelHizOyT47P945Pz4eBiMIyiCDirG7rAokGyhOXRgeiHk7nPpKHr26bpOKV6c31rZ3l7B6M0jtOcBt7jhUEPoCVRaehOsD3y/ThW2vVitQGq6HmuwHEIv+8j9qEgCb7nzof9/twXJO3xyeCjk8FsauH7IkMxCSLjkQZfwBT6ByMQMHfJCzAQGI6PHYcDBAsiqE4UZykvHp2c7x2cjcczhuVkkUOTIDN2GNqoBYphAmvqONbq2vaT17ar3V65UUfRY7IAHNBcyEMckdkEYJ3OzDgCMfHRkSnPN9bXnbkzmUyyBOMrQZMAYsmcY5ndR/sjM/hk7/BoMKZBGdCiSYBY0M6swuUENxdcCdAAAoZOI8OADD0qYxiMNEASSAL2htucrAAY5463e3j24MFu6IeEpOSxn2SoVcALY8/NcqGA5Xb3jzKayZFFFBxgAXEGQ4ITGVhqoshyjMw6DloqpOhBfwwGyjD8ZDZH5yHFHM+hAeCwqihTNzPD5ORijCaBR1QWAdxChmvHZkeVYDKhGhQJUBaBX15yV0AocQDYgB0x2lP0T+CDSOZJxKQRmg3tYbnh5QTOQCNYLkvBWzG/Ts/OBgM4LYgKOl1SFSxG6hW8HCyNoTHBIAkqxZI9M6MoqJSMjZ3t1evXlja3UNBAHCAhpjX6G2aAfFc7PWJknvIoijyl0ojJMsyvq1WGFQVUDOJPgBaVGQYwHbkB+oOcMJwwmY+RUHzkWJiNaUHFLJ8HDvgVkRmCBPfyCG6kgqz4QcAUNBnIeuvG1VK1Um23YEfBKDI8FrJBfZIYKWEd18VIrjcapWod/SKpmgCJI0ud7e2aURZYAd3C8WIA1o+qY/mI5ZGrLHB5oHAUQGKhuKXQu7LVykGBUtQbMZ8QTN8GqYG5oE94KfD8q3IydbE1dCNmFIZYFgpKFvlgy2g+8huiO5LAngPcUVpMSAmVSgX2mV5cbjSxsgIWxMt0RrdqtXKlLvCC6wWkeWl66/YdXpCrzQ4QBPJWq9ZqnW691QFGSZKMCgzjiJLUw/E8sMw8gPrhABJp4MZ53mUCBIhYnxJpCw/ALPI4SEIfGbhsAkjFl5fLsTkFH0QGKEFAKacMB4yIfS+NSQOAiyBMaeRhrqINmJyXHM+bmzaGliTLcAAyYnX76npvBZS92ulK4MwCD74FxmHUaleevFOp1fEBYxgst9Ttdbe3cCZEDwxCGySCDJnLhB4Dgs2Ssc8Frs7my0VGRTjQ0kCIy6ijPSCkPBsSESfDYJuiVtc7z9BOkCQ4j0PBRAEFNes7oDgAcSQq8R1QUV4pLICMZsBMwiRvNFtL7RY+wieIrI3r19sbmwgKFFdnY6egahkNQpp6tt3sLUPHQHRj6LqQUQWjvrpWrdfQgSht0D0HrvqByDM8xMGCPLY1sVcutKsqpxUKEp+jotJYpDEBGJqluCzm6QyVhU9ITXW5d6cpzKHl4xCcj4GrQNg0ga8sWhuQ7bsQBjAVCIO4MSnNor2mtqWidxMyZRQcRlFttlqrG+Pzi+rKWmN5tdvtYR6e7u0WazVFkc/6w//8R1//l//63371G99694P7pUqVyZB01g+jyXgCiku6EMIUMycMVtqliiGVqwbwRpcwDDxQSpFKJGBeGpNKYTIW9tEgRq5QLK31KqeOgykJLgR6wsYBwwJLcxYYw/HIWOTYFMclgQ+RhVGfSLKE4Jm2A9YDGEfKGI5VS8XWzhWj3pxOZkvXbrY2tm6+8KJaLMP66Xj8tW/8yf7Ymaec2Nz84++/ub+3h/jhy6btgckpigAEBIqDtdxs6Y1iwVC4xwcnr//kzfFFn6NSLvIlKkEbiVAaoQfkl9lMhI2unctKd6njgvHORxkCzAsMJqmoEWkliDTLApDBczDHMT1JBpI4ETA1cgoZR82gsIDl6Cf0gyBJ6zevqarMSlKhVjVare2n76iG7jhuc23rN/7852/eeerpJ6698pe/+MFBH60JhCDTEbw/S/IkAFgAybdW6ih0jP5rW2svvvDMF15+URUULY0bbIZ2AFFNZmNoI4mn4RLKS6oUm8udG5G3Hc5s1wUjRf3knEgLIoB/UfVMDIyS1MT3MJvBF9AdqWU72BvvFiqcATfHbLo4P58MR0apiNogouzyQgBNN3vdrc21WqW4vbm+vtK7tbm8sbKMkeoTtpSKDKCUAmIAKuTEbzeq7WYdiqykF7B2u11drRt8mtzWoIAIteyxmXVxCsgHXnFUEmaUUjRu6NlLq8VgPoIQBtZmqA1RkFBtZCo56ehEKJQhg8klKkL1iSqgIDugBOAgTjo7Pv3e93/49s/ePdzf921gA5n9GFIUiH4CE9mNmtFQhNWCXJIFNfGev7aFllJk7blnXsAKixTSfuAVmaRaLoKE1Vq1S6UiyMLtjQY0/kpLWxXSeZZfa1VXUPueC1lXZhPTCo7OpjWDu73dlRyTyaIs9KDFM98DHUYIg/Ep+p4TZfgPZoMSgk4Fy88CUDM0B8ucnp1/9b/+Nz6Nn7q6vbWyzIN0JQnmB8qLXEfCwImTpiqV8qQrMfx8yPq2LIogZMtGeaWgY9gQJgLx6bmKzGsFRddVKFRNLy4mGHNjo0krBbj78vUWksqWKi9vdcazKeKzXNa2V5cL5QrGa1Er1qmIhy7IU3BEyFpe0/PQz2NPrzRRezyd077FhEGAAYQ5NZhNxv1+6jqv//BHLz5963Offr7bauqqyiOacAD9wfEULyK0mAAQZRCzDJLn+2iyEJqDpsuals+maGKMvNA1QXKBIv3+2df/+zf/8A/+y0/fec9zfXdiFg31N55e/fDcuXZ1428s6+/P/au3r76qcyeW2dJFXdeWljvLq0vFnZ3lsoJiFAWQUJ6lMdZE2nekUk3UCqjqBJTCswnf4Hge8aN58dHRkTXo1zTl2WvX6CjNXT/zA/At4C7qh0xyjBKAMZCKYVF4KHZkBaCJuYbOYQWOXGFWdF5W0yiMrUnomG/++A3HiY5P+r/1hb/w7W9/GxoiCePP3tnUKCrixN/8c08txe5ZSn/x5Wee5vLecpuSMJfSZrte7nVW60VocQkAL4mGKglZTIWOCK2MwQf1F6dgluzf+ut/1TNnlm3fuf5EWZCL5eLaxoaAVIeIOillwDvGB9oa0EuTNiIoRcjXZaWj4FlmNBohCWyaa7r6sZXMKN4+2i0w9JNF1YrTT7/8uUGUbb3yhR++9nY9nG9tb/EFrVNUUJFKvXa9U5lNZ1tP395Uc8yf9vISikIShHKlfPr44N2zqSorYrmhaiqYlnu+L+SxUu1wqm6Px+CCTKnWhF4O0b5J2uFkfzJTOBZSAqAM84jRvEDxPCG4hDRirjC5wCNOtCgymJSinItykEGzYwUokAxqNgHlBjqLck8Qb2xtLm+udpZ7re0rytbNr3z37cHhASBBMAxBJVf1yhurz3/mWaix2uZW2TAOHh0MLvoY2SjPmqHpgoAEyBwnFwwQASp0QaQ5EP445ATBd22mUCyquiFL/MAyjXIpmZiRbZOLQQitwNGSQEs8BQZDaA1hgotmhgQUKFGkZIlS5ITl0ELIhlEuz3ywGHDFAGJiH6PRdbuVkiTy26tLX/cTemcrfe4zMYCO0CEECG9QmtiLC2ezw6PTb/7xt37/H/3T3/3df/bP/8nvf+2rX6M4usBSsgwdlguyBBOgYDFlJVmBwGUUhRB1RVXhQKfVHM5GiYhhhrb0iWWyQMF6BBuMAAwkimfj6fDkrL9/NDg8saZzEPYcbFmSwFRhBAJhrK7sn57ypN9p0Kgz2z0bTRjbyqKw0yj/gyur//C3fv2lVz9b7bRIUxEJQyQBtAcEQcrye3fv3zeTyl/6knXzhXu+/L9e+9kP3nyHySMRWMaTa7NZGvBQ0ODogkiECmwDTfrHv/d7oGh55EI9o/k1mk9pSus00fMw/dJ6F4ru3buTB4+807NgOA5n88npWbnX5dEYLDebTp3hANYcD0fnRwd6pbE7cezhYGZbpTi41ajIjbqYp7rnsPBEEzoCAAAQSAQMSoiUJZRtlBhLq3mtXlpZugjTSqv911769P7j3aHlq7WWoBoxy1WnpzWBGiVUqdG1vACQzjlTcA2hUKqAM9cqlU9O9tOCErk+NCQqJGd5CnSRYS5294dvvpUMB0KWKhj6eWoNBs7MBBYh2HEYxnFwdHr6h3/09YIi8uD4vksUQpp+Z//8/PyCc0x2PGrbk/bgeNudUoAyUojkGjDJAd7HcUFTlzfWW82qpMuff/7Wzetbt29eX93apDCt44iXZMi0HpN0NQk4Q0MrSxL+tLm+DHon6KWyWqqCUVrW7GA2korFwHbQAwgPwSFkOQqxQWtlqVjUdUU2NLVUKmKSwzd0fxKE48l0apoYF0ZB9c3Jgg6l2PiuFZ+bJo0aYzgRVJSHDqLAeTHqaFQRPMEPNFAYQElgIG7XKjeajfVO8yVQqHJx3YA61CHSWY6PAr8o8U1Dk9HfgS8qauLat65dIWpUA3itX+luXq9W6/cfffTJ4UEM4U6YD1qTVKlcMqrLy8219UKtprZalaVeZ2VJRAnSlGtazngEJuW6nipx0P7efAprW40mufmjaI8nk9hyaEh1iOwgyoiIJ7qQtEGa0YhC4Oe+nwdePh02/Vnl9KjeP79WViPHXG5UGb3CqzoqAiyhpHArFQ3KHexBRGkn0dXtTSKMIC4BUvWl1Ra5XSe+8f4733/tdc+CWoX2gAjKoQGMWpVSC3ylAvhjFQnMU9JU3w+gmg93HwGx5rNJvVLENDPHfVFSWs1mQStsyNJ/euO9cziAbcAI0fRoLbzBHJQNMBwSJrRjEFJ+lDu+FIbLTHZToOTZUHbNaRCltIAJiyGTek6nbiQM43s+zXBM4JR1dblTx44sMBCtzYtSqdXdeeKOLIkffnTvg3d+Rq46oYqyTNELdKWcei6+iN4P/ICrlgVNBRMc9c9OTo8wYubTCaAMktIeXiBp4Jvtao2TlQf7jyfIYrlEF3XaMAC7hJIoOmXUcjBk0NsEPiQ5GAj5yQgjhp70A8p194/7Uc7kNOsFfpdJOrXi3f6sAsgX+MSaNOuVellH7BCdHEUmqxoy0VlZ39jZMVT5Rz/43oMPP1rcTsZAo+VGjdxLQ0hQrIpU6jTxTd+af/97f6YYpcFoxHFct9kYDIcQlGCI4EqdgkwulYDnohSrVbpUojSFQvMJArg3nRKFTpxBHhgwHQJI+IfqytFArgd+/9HxBacVg8C3/PBWSTgZjt46OFUwk8AVrflKswrMIDsgQHEYkItEvADh3CMatxJF/le/8gd333rHtx2Sf1FEbDLXB20Wy0WAX2xbxx/cdx0XytWaz1uNekESzvsjyHvW7M9FlY08PvbrFHU2nlOyRqkaJSnkGjovAqiowCYlhCSA26gKLUs05rrAY8ATUUXRozi5dzpSCnpoWxhDChN/8833AQYY9HnsB1G0s9oSBGjpDHQucOZTjOUMM9SapaFfrtbL5ZprmV/5j//+u3/yneHhSRonTLWUQjLrOpDLBew8eEyHcUHXJoMLUDdQ1zRN+uMpkKuN9PP81PEUUbr27K+dnA5igLYgLQgVi7HF6yoAACwLJIXkBHUFHzBZFQXOIEVcqfjg9ELWSogvKlmM/Dc+efjB0AQYAvpAHttlfXutNzNtxvMD25ydHzzuH+8BAZPQP9l/HHiuoRdURQFkfPe7f/r4f789u/ex47lUswHZPzk8MR8dUGEcZin0sWPOAT71WgXoOcBEY+jlAp3Mx04QlNq91avXLCe0LAfVki/mBnTgwhMUN2qXgaxcsBJYL5MfCVgmTYPAPBu3222wEiihwJp8cHCg6yWa44M0bxja7Ssboih+cP8h4wdB4NoIvGvOfGsW+o5l2aFLrsPIigpljBhbjs3g392PJycXw0d70eEpuYgrQRtkpmVhhWq9VlCkOT7MpizPF/hMT4NevdLQFVEzBEm+OB8Qakgu/bLgnv2zc7B1IBIDtoLRx4Pr4AdveOAG3DifmhVZsMEjiYKJmMgBzwP2ZzQPrnprZ31tqQWOV9IV0KlsdnGCeVzprhYb3QrG1caVpa2r9Va3UK6CgXAsMw98qVpWBZ69uBBMR0SQVBnisFOt9qp1AAVADGg1HIypNAEQoyULWWg0mxsFli8Y1Wrl4YNdbz4nbcqx4/Pzt/7Vvzn51v88/9Hr/Q8fmuMZ8gDfwHMpNKjA2xjj5tzKckjmLAqSyM0CS9HLaG8nijdb5WqtvNLrgPt2WjUmdu3z45PpzEQ7SqquFqvNpbV6Z6XaaGjkBj3DMoxMU6wkyo2qqClSUWcKCotu4ziR5196+unrqxtp4GG5uWmHgaNpasZyiTmYU3xbyXmJx9wbTOc/e/d+FgRUnpZbjY3nXqiX6hpG2d7h43/3H84/ekCUxuJgBK4/HM3Pz96ZJdgXfDPz7cC1gObAWdko3n7iRhTF7XYLDUAs/Du//aW9R3toi/HZkUju/+e2aTE02J09mc9RUU+ub13pLmGAC3qBXEfmOQYSB9hFKHGmCEJJ0+LQ5ej04Pji/YePb9+4Lgoi6O5Zwj/T0WOa1dvdqq5ezOzEsZplXSqVtGJRUlSh3VJ0A9QyVSW9UcNqKDFwkzd++Pq7BxOz3Enteew5zug0iYKdrY1M01dv3q5KfElkSgVFE0GRZPZvf+mLUZja1ijwbcBqFvqTyQR/yNOEyagr9fat6zcljVyI5GVyjwygu3AAPCOjEZUo9F3H8eaeY3WL1c8+94IbRaLAIlWTKYalstUuPZ6Hz9y48uyN9f3Do8HxSaNZ5zCtwNzNaZJn0va60WuTuzXQqJ778P17/+Mbfxqt3mTBgseD2J7vtIpXbt6sNBuTlHpha7kSuU+ut0psVKBjGu0BXC9Wyyxij5IQhNCzj0+Poe9ROevN1ubaOlvQOExiiDLssTgJfyJvZBlpABlOMdEcx55berH07Keeb7a7QRTDgb94e8sPU7T49QaWoEPbudpt3n/jzQ9/+lYEIuTYMRpsbUnQMZpIRp1h/+FPXjt7796nrl6PRS11Lc+Zr3HJ5198TpX5u4/2lhT+Kp892SsVNAk2oN9Yz8Ts4lfXVp598tmdjasoQ2hxkWJ0WRZQx7AYfAFYIYK3gTdkRAEvbkpzqgqXAPlqoy61mrPJLAlDsVYrtJuNdns8HKPhFFV+fmd9bAZaFGgiG12c58OLV+9cl2aT/v4hBAAPRUGCQcdBMHn48OQnr412D1dAqIx6CjifjyJz8pmrK0XfPvr44d7e3h0p7pZEtd0C8JCZTW5DZUyrDRmzunPtZrfbi8IwCqKba+sCQ5umCUJAHohTVR7DSxLSiFySwGQAHeIKSh6GQqUkNOqcqtnzeeJBrZLGWNrc5HIWJ0PQdLqt565vF8qlLEp0JlVFMAO91aiy87HHkmdZ/Lk52D88eP2n9r0PRRrUsyQI8nshnTrT+fDi5eWqoIg/vvsRNOW/+M0Xnrm5IzZbhCOTYkA3Ep7HVOt1VhByUYgS8AkiD3WtYM7nURCSPKB4OI5ce2CYxPPxZTQAr2ugK4nnwRkURur7HMevP/8ZwLlQq9aWl5bXN3OKiwMnUZTVq5ulZgOzH/hD1AX4DkvrRTWdDs/fe+/B9344/f6PmNGQ07VpRrWKhUc+04fknw83NeGV66urdePGzsrv/JVX7jzzlLqyAdJGyBIqjqEzQQYTQd+TuyNQ5b5HuL1uFDOe8X2PBYOsVwVD51VFQNGVisCf1A8ZsAlBiEwrTVIQJI7nVEWSCsbyrVvZgjBzgrC2s8XJRQiz89NjAHzu+TJ5piNDtRBti9HCcVIes5M+Y46RCCw+DdO6pnK8+FrAsda4V+Be2Wl315abV648+2svtNbW+XqLllVy0W9xgZY8VsVDuBeYLCTaJfA9EAr03NJSzw0DyzR1VYF2YWUpT+IsDHhNKe1sgvBlUYyQ46ew3MXQAQqppWKx0VILWs5wiARiXG+1IFOVcvPx3Z9dHB3yScwR1EL4SQuRFxqcgFOMQqVRCSGMGK4skYtvr42j+6a75g5+faV2a6PLdZb4WlMoliBZGNA+IOClxiIkmSXX12SNIdYgVAf7w/HsqdtPaZo2nkwQraXNjQVvYzmF3LpMXA8FoLSbeJ8GIcIOxoLocoahVCrL165FfoD6Rh0SAdRsaLKgGSVrbv70z36A9meFxfWlRQmRQlocgAe1aGCso6zA8a2M/dHp9GV68jsvPXl1qSU0u4xRJs26cJt8gQQ/I1wVppNH4EhhIwP+2e4uVMkTt55cX+7hV7Y5v/OpT5XXNsh5EN1wVpR4VUt8HwlB47KSIKoKufhFbm6RR7rWdnbuvvmu4wMDAnxESZQqJdShFcSgBTiZVcn9JXLTm0LFkqspsAoHJ4qKIo9NiK/4B4/OXtTCv/n0mqYpuaTwrR6YE84nTv/CevIGIhJliUxmMYgqE5pWEoVPP/tsr9nEqk4YtDut5avXiYvE/MuvZuTeuQRJQAKM0ie8hex/Ca+5Xqk8c+cpNqdmcwu/xwa9zXWGFTKuUCzVeTB+xLlc5TR9EdCfL0xMY1gJSiCJRvPpEzXx1ZurgsiTpzOKJUaUiKcL43EQ+/GyuCyLry1+iFyk52/9WCiXYW40t3hVGlozELvO5g5BfTTlz4+F7sEKGECkhVAG5MoC5gEU1GIPGiBAZenFbFarVoDuEG7DvSN3Mke/9q5t84aBCZ55TnS6T3Q2cYL4Qp4XCwJ7ZuoVQ+BY8jgoxJOkSEurnFFCBhZnLo5LBxAtcmMcbnDksiepLZA+y45tG+smQVgulTlRCh0Hc47cNcKCv6zZRdgWVxTIpTjyMSeX3VETeGVBSxSlWCweHR7FvheHkaqqVQOygjznQ4oV41tRuXKNFB5sx2dkkiP6i1dk4CPcShf3sVm9iFyBGvzKehyXPqPusRvqcPERvyaKLEvIkxNpFKVBIIoS4OfyYgSoDoJKrmMSZ1KStUsQwLHIKDH9MjQIDIkNdJVar1WzmDzEB+4UB+TRBjJJLksB7aQbfKmK9uV0XShVpXZXqNTUWp2HxaLESAql6kBMZvH01q8Ost1iL1gbRYQ4LX6JHwZZo8mTE7kzm0WAmjSTFEWQf/59cgqMTtM8DokzvwgJWYCE8fLjLyqVbJEVS0UgPXbC3ADsEs/xl8Uz5VgMIWRVjTPKfLnOVWtCpap2V9R2T6o3xXpbana13ioA43KtXx74gBXyNIa4yXyXmE7uFJHMk5tL5Iycij3fd1ykCC1I2PniFgaJ3MJzUn1RcBkG8hm/J/+Rhckf/6/t8BYBzpOUPCWAAkPOU5JP8reFw0gOWzAYBWyKXCMCQPHFMl+sIBVCGaOziEIhJ/9/B2pr8Ug7aTZUBDmyPM//D/szpRv2WEuhAAAAAElFTkSuQmCC";
    public static final String LAUNCHER_URL = "https://maven.cloudclient.net/";
    public static final SimpleDateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd't'HH:mm:ss.SSS'z'", Locale.getDefault());

    private static String launcherVersion = null;
    private static String modVersion = null;
    private static String gameVersion = "1.20.6";
    private static String modName = null;
    private static String loader = null;
    private static String loadVersion = "0.15.11";

    private static int modIndex = 0;

    public static String getLauncherVersion(){
        if (launcherVersion == null){
            /*try{
                InputStream stream = FileHelper.getStreamFromURL(LAUNCHER_URL + "version.txt");
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffReader = new BufferedReader(reader);
                version = buffReader.readLine();
                buffReader.close();
                reader.close();
                stream.close();
            } catch (IOException e){
                e.printStackTrace();
                Launcher.getInstance().die(e);
            }*/
        }
        launcherVersion = "0.0.1";
        return launcherVersion;
    }

    private static void setGameVersion(String version){
        gameVersion = version;
    }

    public static String getGameVersion(){
        return gameVersion;
    }

    public static String getModVersion(){
        if (modVersion == null){
            InputStream stream;
            /*try{
                if (getGameVersion().equals("1.8.9")){
                    stream = FileHelper.getStreamFromURL(LAUNCHER_URL + "mod8-version.txt");
                } else {
                    stream = FileHelper.getStreamFromURL(LAUNCHER_URL + "mod-version.txt");
                }
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffReader = new BufferedReader(reader);
                modVersion = buffReader.readLine();
                buffReader.close();
                reader.close();
                stream.close();
            } catch (IOException e){
                e.printStackTrace();
                Launcher.getInstance().die(e);
            }*/
        }

        modVersion = "0.0.1";

        return modVersion;
    }

    public static String getLoader() {
        loader = (getGameVersion().equals("1.8.9")) ? "forge" : "fabric";
        return loader;
    }

    public static void setLoadVersion(String version) {
        Constants.loadVersion = version;
    }

    public static String getLoadVersion() {
        return loadVersion;
    }

    public static String getFileName(){
        modIndex ++;
        switch (modIndex){
            default: // case 1
                return "liquidbounce(cloudaddon)-" + getModVersion() + "-dev.jar";
            case 2 :
                return "fabric-language-kotlin-1.10.20+kotlin.1.9.24.jar";
            case 3:
                return "modmenu-10.0.0-beta.1.jar";
            case 4:
                return "sodium-fabric-0.5.8+mc1.20.6.jar";
            case 5:
                return "ViaFabricPlus-3.2.1.jar";
        }
    }

    public static Path getClientPath(){
        if (Objects.equals(getGameVersion(), "1.8.9")) {
            return Path.of(OSHelper.getOS().getMc() + Launcher.NAME.toLowerCase() + "VIII");
        }
        return Path.of(OSHelper.getOS().getMc() + Launcher.NAME.toLowerCase());
    }

    public static InputStream getIcon() throws IOException {
        return ClassLoader.getSystemResourceAsStream("assets/icon_1024x1024.png");
    }

    public static ArrayList<URL> getJars() throws IOException {
        ArrayList<URL> mods = new ArrayList<>();
        mods.add(new URL("https://github.com/pSUNSET/CloudClient/releases/download/CloudClient/liquidbounce.cloudaddon.-" + getModVersion() + ".jar"));
        mods.add(new URL("https://cdn.modrinth.com/data/Ha28R6CL/versions/a7MqDLdC/fabric-language-kotlin-1.10.20%2Bkotlin.1.9.24.jar"));
        mods.add(new URL("https://cdn.modrinth.com/data/mOgUt4GM/versions/NgnZx44E/modmenu-10.0.0-beta.1.jar"));
        mods.add(new URL("https://cdn.modrinth.com/data/AANobbMI/versions/IZskON6d/sodium-fabric-0.5.8%2Bmc1.20.6.jar"));
        mods.add(new URL("https://cdn.modrinth.com/data/rIC2XJV4/versions/apfXMRSv/ViaFabricPlus-3.2.1.jar"));
        return mods;
    }
}
