package com.argus.image;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * 图片文件传输
 * Created by xingding on 16/6/22.
 */
public class ImageUtil {


    /**
     * 根据图片文件生成对应的base64编码
     * @param imgFile
     * @return
     */
    public static String encodeImg(String imgFile){
        //FileInputStream转成byte[]
        InputStream is = null;
        byte[] data = null;
        try {
            is = new FileInputStream(imgFile);
            data = new byte[is.available()];
            is.read(data);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * 根据base64编码还原成图片文件
     * @param imgStr
     * @param destImg
     */
    public static void decodeImage(String imgStr, String destImg){
        if(imgStr == null){
            return;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            OutputStream os =  new FileOutputStream(destImg);
            os.write(b);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//        String imgStr = ImageUtil.encodeImg("/Users/xingding/Pictures/sun.jpg");
//        System.out.println(imgStr);
        String imgStr = "iVBORw0KGgoAAAANSUhEUgAAAHgAAAAgCAYAAADZubxIAAAZFklEQVR42tVbB1RV17bNr+//8f9I\n" +
                "ft6LxhRjNDFFjSWxRo0aY43Gkth7BUVFLICAAiqiCGJDRQVR7IoasYKKiooKSrGjiCICglSlg/OvuS/ncC82jOa98fYYe0TuPfecffZaa6655tp5C29gPHnyBDmP8rAz8CzWbQ1B4OEI9ZnxWJWXizOFhfrfEbuLX3rP/cEX4ea1F/aWy+Hx7XAcqPMrgmr3RH5KOuxct6L9b3PRvocL5jjtxZseJSWl\n" +
                "WLvpKL7vNhPNuzrAbt427D8SiXtJ6W/sGaOsVqHGdxPw3tdj8EH9cZjhstXk+9y8Auw5FI46raeiwY/WaNLRDrM9dr7SM956EwstFWMkp2ZipCy4Q18XTJ7ppxu4pLQUecVPG9O+UZ7J38Gh4bidkKT/nZH5COOs16oN+LCeGew/7Y391btgmk2M2vwew9zVxtT9YZo41fGXrrFI1lBUVD6Li0v0yftx\n" +
                "Gjvl/ZQMTHXyR7VvzNXkc/qOXoyDRyNRWFj0Rgy8L+gCFiz7HXMWBcDZfSe27TljumZZZ3jkLdkHH4ye4o2hE1dguc+hP9/A3IhIf1+cX+2l/337biqadLJTm957hAcSkx/KppbA6VgINodHIvZ2MqIu38GZ8BsIDIyB9cBzKuIvXUtQ97h28w4KjDbu9t0HGGC+VN3v44bjES6/s1+UiNLSJ3iQloXO\n" +
                "A1zVdw3bjsCuwJdHsLNHAEZarsJEu3Wwc9kC1yV7ZLMOY8P2k9hzMBwnzlzFo8f5evRuCgjFDz2c1DM4PxVHGzttNcLCY/XrXncsFHSq3Xyyuv/7dc3Qb+ySCk5ZgsMh0foaOKfMWv/mDMwXvXMvDVduJOJiTLwyztHQy9jkswtm37aFzYAJWLU+GItW7oeV4wbUaD5JLeKrllboNthNeXy9HvZo23cu\n" +
                "Ovd3Rftf58qmOeP7n2ehaWd7tOs9B9vFyAH7z2G1/xGs3XhM/XeFX5CCq0Y/2ar7VW9kgZnzt6FNX3+4LN4Nq1kb8HWrqeq7r+VZ42184PbTEnjYb8Xi1QdlPfvgtnwv5kt0XIi+rRzwt1GeJhtVcX7yrQWCjkcrB4q+clc5wocCm/yuSp0xqNVsEmxmb8KOvWE4Jntw+vwNRMi96aCxcUmyT6l4nFsg\n" +
                "vy+t9OZv2nkKjdrblkG0OX4ducgUGeVeZyNumqxz1vztLw2+Shs4L79Q5YlO/eYp47Tp6YyW3WbhyzqDUbvuUNRvZ63yA72wumzQe3UMi/hAIO3L762UEX+SPMmIHj5pRVn0bMVkh/Vo2N4GDSWvbN51Cm16Oav78DefN7PEp40n4qMG49TGai9m/O+Ks9o3Zvj4azPUaGiBWk0nqfXwXl+3moItu0+r\n" +
                "lx5ssVy/nusj9PMZ2mf83Q5xtrT0HCxatU/91vgZH8q1dMxO/eeh+xA3eadFKuIGj18u77YSk+z8lOEzsx7rxmEEEmY1+NcmnYifkaswv2vv0LmfK+4mponDJCNGHOfcxZvw2Rxisgd9xFE9vQ9gjucu2MzdrJy7v/kS/NRnLsbb+uBGXFLlDVwoi9NgqnnXmbAWD2a+WCwP8Nl0THnz4WNRCrauXL+H\n" +
                "e/fTkZ7xSPLnY9y+8wDHT1/BqbPXkZOTJ96dr0gDnYaR8n03QxTzJV8UWa87fbeEqE0dNWUVqtYZqz5r8fNMWNj4opc4nkZyvmgxGV7rguAn+ZzvWqXs2srOHkMXyvteVcbjeCiO4r/jJL7rMAMtuztKgMwR53BFl4Hz1WSK4R5UbzRev0f1b8erIGr1i5P6rlkXB9Rva/0U0tCBazaZpP5NlKla17DW\n" +
                "YRNX4UKMoMn9QlyLKxAnevJiA9MLGYH8MQlHXl4h8sVABQVFyMrOVfmrz2hPBYeaZ9IpaMQZkucIraOsvJGSmoX7yRnKOxkhzL2MfubsG7fuY5qzP+ZIjvQQaOUmr/Y/qgjWhwJbBiJlq57FuV6mxQxffNbMkA6adrLHXPFmOt1Cr0D1b5s54tm2voqUHAqJUmsbL/erJmSNv+GaEpPTJUJuoaOgk2Fz\n" +
                "LdC0i6STXrNVhBsiZqyKVl4/3HIl+pktUamncUc7k03/TKJ/paQqOrYxA94uAVC7xeQ/1YG1STTqNMgXTstuY9nmh/AJyHh5BHNjvhdvryI3ILRmZD3CrfgUXBejMB/TCNwYC4GGUInUgxLNO1SpdFwnQR36uGCqoz/M5dqhE73gZuGA44FHsPvAeRw7dVk5DBkziQsdo7DQwHA9vffrm9xfNrYc3kol\n" +
                "7wdJnjdA6M+DFiDuToqCRkZN8oNMxRu4xshL8epvOt7kmet1SB46YYV6D/IK5j1tk94XB/iwQXneJXptkRQy3z0Q2wLOKpJ4VNY8TwiaMWwS/o8LSas4AoUlNxEHLL92rHoG8y2JIyPw/TKn02CaqNbqF0f8KBHfZYCrinzj71mlTJ+9ETMXbMfcRbvguvR35dgkbCfDrpoQ1UoZmJA8S24WdDwGG3eG\n" +
                "oqvAC2Gk4Y82+KjheP0lNeio12a6gqWaTSaqF2L91nWgK7qJIboNXoAZjVsh7fZttRDmqOeNkZO9y9jlWEyQiOVaikvIKqPwg2yAlpOZ34kmzxuRsVewPWQ1+k1wlPUaInOA2VLEXL2rCBLz5/OiwlZy3BkhU+s3hGKl91GFPsyP5BJ8X15T4zsLhRpcA/MqnUlzxoioOMXYm09cgp6O6zB/6R4hgQfg\n" +
                "5XtYkVP3FYH4ruMM/Xkkp3QKMueQU5Lezl3Hui3HjZDCUhx/H7JychWKvjaL5iKzH+UpESNbbsp8NnDcMphPX6NKBkY3PZEEiosmIdgUcAoB+86pyCWMOi7cgbCIWPWyZKdrWzZR993n5/LC3N+5/zzda7sMmI99wRdkDXnqWVoutXbepBhsSRlzZSq4dTvFhEmuPxSAEfN7omX/QfigwUiDU0jUck03\n" +
                "biXB0sHPAHENx6GJbHZrcR4tp/0iedVmziaVPhaKMZatPaQMQ6d9v+yaxvIb+3lbceTkJYQI5zghURR69pqsJVPV3hy+2alYkHH/qfd8KHyFEakZkLV2xSqGDq19TyJK7vOn1MGMntSH2UhKyVDeSlI1ZIKXYqu/H4pQ8MjvmGcvxtxWzJsbsVwMTxJ2TDxyX/BFLHNehqVzrLDacRDOHPJ/+jnFpWja\n" +
                "3TSqeo1wl3InTsFzT9l07fOlaw4qSNdqxhkumzHUwkt5f1FRiTL0xuA9WLt/G6bN8dOZMaEv+MQlxCekwtFth4ElC2wy/xLqiEKvmxPXiiHokMbsWZvaYOCQgOm1tlQPJo4u77Zl12n9e66L6Y/7kC5pjVpB5OV4hUR0hj9sYFL9mKsJkl/DVHQyz1JCbNbZQQkAVpLfZrltx3TnjQpO+41dLNHLcmcC\n" +
                "Wksea9d7tmKurPkatZsMyxEDsMKuDy4c3/UUqXNy32m0UWPF2Kswwu4KrBbch7VHstyjPCeR8WolyBohZl+IsxG6ewxbqDydBgyJPId9Ycdg7+aPBu3KGSlZKEme9hl/xxJwsyAQU4n2GYkiI4uOTMZd1Yhdazm16jMY92JxvlwhpUSjhxk5ai2slePvpirkYD5ntH9lVI5R7HAXoknUY60/TNCKOdn4\n" +
                "eVwTczinQWUzUynjtSKYJIgCA2tFejrZKF+WbJP//qTHVNQcYqOUJzLO/pLjmI/JlFlakR0vEtK0WKabhw9cLPsqA8dGnyqHIzGug+s2E/JiLWTifkom8gtKpcwqldxTIiVMuYFH2QRjxZY0Md4REzLDSfZ/+Fi0Ks84vDcE60rb8yYZOa8bM2V1Gekaq5yS63eV/MnP6xs5CcWWCTPWKadmPUyn1r5z\n" +
                "9tipjMsos527xeQ5fDc6hjHB0ibTA6dmzMqgBXP7axmYUHL8zBVs3XNGEYCTYdckFx1RNVq9NtMw89IRLLsRppgpPZXlTAMhYX3GeKrPyHBJDHIe5eNi6EF4zeijDJx890a5oDLF27Tek8jZtidMh7TEHC/cyZqNZl2n6td4rgnFePv9qNWsnKhUqWuOr9vMhZntHmHXaZJaDNBFgsj8qjZRNo+RWbu5\n" +
                "JWqUkSUle7a3UdowSaXGqkkmmXo4mGPJ6LXvWku9qq2PUdlruId+L4o5TBssD01R6Y9NVjKfNjRTaPKNQDUjV5NxyXtey8Ca8sIF01jx9x5gzcajCua+aTsdB49GKehmYe+94YiUIV6qfKLyQyZKCLew8cFoRvdoD3QbOBe9hy+QPJKjNsZY8zXOR8EnYsrTRGkGCkuShaHb6tf8OmqRSeQyJQwcvwLO\n" +
                "S8Phvi4JF67kIj2rRCEA9WaqPbyO6GI2bY3iBM4Ch4RfjeQQcbx8Dun3pABCZksCx3v0LGtyfCAb3KmfiyJTNHLcnQfC/FfpvxsiXEDBs3ATMmfe+9ufbJWAwRKI5R3lU9bqY2UtjY3QhUHDHL5l9xn1bMrDx+Y5IdTfH+cjb2GJwH+1slq9vuw/y81XNjBpOBUnMk4W92SyLJHIbut1moJv2k3XPYiF\n" +
                "fyPxfhqbTI/RoemrhHKqNawvCen0fMIPr3NfGajXnRVnLXEeChEVR4N2NibFvTEBYa1L8qcRr42BmZi6MBmTXJMwaMpJ1G8/V3ce1uZEJrJpvpcyvBiT0uzWPeXEhtHODWd+poE+aVQe8SyRmnWxVw7aoutMhQjad8zjWl1/7eZ95Uw0xNkLN3HxkoEYXZfP6RhsxAyS6kT77c9lv6UeUFFbVpXBtvLS\n" +
                "iU7Lku+VDUwGSHWIxqBhKCxQOqPyQ1mOhmOOaCqfMT8xcime08sZeX1HeyovY9RevXEPiUnpSHmQiQdp2QJb6cK2U1UDQVvo57I5ZtPWov1vhhzLfHY1NtGkZDgaeklFaUVnYF94wbK9uHf/odoUnYUWPFFyXc5jqZ9PxKLbEA+drHQe6IU12+OxctMV2dCleseo53B31RLUovqPTqYDMt3KpsARliv0\n" +
                "/MyoVjX8wyCEp+1HYalp94rKofYcoiXf+5UNTOacmpalDJOW/kg2KU9FNUul9dtOKIO07OaomvI0GI3HzSUTLpGSikU6PZ6Ryl5nsuQxU8G9FJfFi1tJmUCGzVKL4roxjCYkGlqOJyT3UVT/oaeTXqNqEiHr4gBh9czv4ZeyEX39ERKS8mX9pVi3OAsblmer92G9PGyil/7bjv0XY4nfZZk38Mtwn7Ka\n" +
                "21zW4gQvv1No29sNHzeyEoSZJJ9byHPNX6hPc11ELgo8X7SwUk53+VrlDgckpWWicf+ZuvNZSv6OSg/G2tjJ8L5hgcTc6zidkShpqkRFNmVj7bkOEiSZWbmvbuCK0KCgRiLKwKYnoMNv03DoWJQygpPksWHiSV7rDqu/tRMedASWIYThtr2cVSvwfhlh4TWPxGkIWYxwsmgamFCobZjTwp2SpxapvPWJ\n" +
                "ERnS5pip3gqe6Iwco+2v4bOfwvC3pqFw97mL5esuYOzUIPUdHZPSqgbrrXstgKdPOPYeuYcpTgHlOff7GTBzOAsr1xsY7xSDsQ7RGGEbicFTwzBUnImCSMV10LAGpEpUe0SZNOZqMtZszquUgWMTkvFFz+n6e8/2CFCfn3kQAN/YKcgsTMHbwYZecXxCuuTwpSb19otUrZeSLBqW2i5PG7ALwg0aZuGI\n" +
                "E8cWq4hlx4g56IOyUw/891ArX7j5hqkuEklC619m6bSfL8BSg5tNrZinQUrLnOl39wDUk/xorNxobPFZk8yYPVhlwIxinVAVFZcq433Wygt1OqyH15aHWLMzDb1Gr5PUYmhS1GxqjdY9fdFr5BG07eun35M9aK7ZeKRnFWGsUxxqGrF140mJ0d51B7IeleBhZrGkoSKUSNVQklU5iL5wPR6fd5uqG5j9\n" +
                "cG2EJPmjqKQQ7wUtU6ri1t0R+NxoHewJaB2sV4ZodlzYEaEgT+OQTFFoj42NQHG+ocQhhBOOqQC17uGoSEm1+uMEOnYoxYsKGP9L1enLllZ6b5WlCBsNxmObEJ/aZfo2ddcFRrmGJYzVzA16e49ztudhYcqZSEwpwrrdGTqhsnS5CfNZ0egzMRJdR59Go5934MDJDIyevk3gc5pe5tSoPxGffTFd8QtN\n" +
                "rKgjta33+mATJOMkchn3iNv2dFYOb3ifSfjh1zWYtfwB5qx8ALel8Yj+/TIidl+V9eXhdGQujp17trGz89OxMWQzOg+Yp6+L3TZtnBUS6LUxGJ/NmKOEJNb3WreLusTjl5wuea6BzwtckkzRo5gPKcrvD76gCwckQCRTrBGZTymGsPVHssXu0oWY2zrMazmXJcOG7Sfg6LYdEdFxT6WBjZJna5S1CLnp\n" +
                "jsvi8G3nJWj2izcGTDqEZf43ZZNtlMLFa/pb7ILDknjMXJaCszG5OqEKDL6KDv2Wok5bd3zT0Qfv1J2PiJj7qqWnNdifN6mfk/wZG5fslwRTu4YavL3rNqU2aaxcU5NKBNUShQyOnBYGz6U34Lc2DjuDsrHvRDYC5L9k9qt3pGPJxofw35shqakYp85fVaWTFsHUGbRBVet5a6XA8oe16Aypd0lsyCpp\n" +
                "NBbs2mBXxVxqNzbGSSYmO/hh7+EL6nOSAM6KsGFMsJhvK37v6Z+GoeMOigeblzW2LRF+ORf7jt7C7qA72Hc8GzuCslC3jYNOdniqgdo3nUeDed6XpQ8lwL99NRZ//WoC3vnKGrsOxiihhlxA20g6LtUtpgKt91xdEIRkTFtrzLW7ivhpEf6x5GCSHJY69q5b9Rw8ZupqXW69IpyixY/WcLf3xZ7V+1UN\n" +
                "XSzOdyYqF1HX83EjvgAJyUU4fykX09yT0dtCSGsLuzKhxgyHTt7Fhat5OBnxCB5rolD9uylPGZdHoF7EniuVg29KfcY6reKgWkVBgJCttwqlhmzZfZaSG6Mu332u+L03JBuT5ycpOLVelAy7JSlw9HqAWwkFQkrClB5riArLZ9aAHfvO0yU+tgp3Bp5TLby4+BQlC5J00SGNZcA2vVwQsC9CqXDscmnl\n" +
                "CNuGR09ewoEjkRg0fpnOpH8Q+OX6eZ6rY18XJVlq9+Jvoq7cUchFB9PqfTZeNEemxqw5EUkja/NnN3AM77dr/zlVcWjdsxVbUrB5fyZ2H8mG386bUqd7KBb/aRMbWecKpRQmiHFflHtf61QlI5TngwnTXNwQi+Wo2djQ/6U2zUV2G7IQa8VgE+bexwSXJEyUyfwYevGxibacJuQoKbVILZYCgxadlBBL\n" +
                "nnGAjWqR8TEXbiLPcVEzJsHjsRg2OLTTG/yeEUdRge1KsnJCKp1yyITlisXTYedJLV9DamyyZJY6lCybVDi50XXQfBw+Hl3W6stRLUTtGR0kysOj4nDk9FUTTb2eEM8X9as5yIS1AwxECpOWoqS+oBPRgpAXEXrulqpS8ipwlz/tXDSNki8UnaJIeNQtdB++RiJwXJknCmzZ7Dbk3ycGwaGomPD8fK/z\n" +
                "NTpgxgjkvStGsf+OULTp4axLdS+bjAweouO9MrMfq1KOdSYhlcTpfGSc/i48ZnNWuEffMYufKstIbvYcCDcRgtgbNnw/GlVkspyr195eJ4LsGY+38X3ucajCAkMFMG/Jbv15n0vaMI5MTSZ+Fpr9aQbOyC6G84oHcFiaAlvPFJVHLOffl2gsVHmBwgYZX35B4Svdl3U1xXxCL7Va1tLPqsepzTJF8GQj\n" +
                "G+ZsRzJqSX7YXmvcYQaaCyliT5onD1lKvGwwytjy/NSoTNOgvLPch1KjiZQr6zghZOhzI3my4mSjgghRcQTt2oppg3oiMixU/c0DFBpy8bRMZU9rvHEDu/ulYd6aVKwNSBcjlyjGmpcvnlj0ROWTFzW3U6bGIr55BApinw9XPNDODeEhdOrg7Kc+y3OJCGTzzIPXbyUpxk7myabHzrKmB0WW3w+FqzRS\n" +
                "We/ndfwNGa1q6QkfoOR6+hkOwmtpZEI5j89yUlUbJ8SUKYFnso8Jmy6oxP8FQd1aO+prNn2tQpK/u4H5Qqw3H6QX43Fe5Q53GxOqabMTYWOfgFlCquZ6p8LNNw2L/R9i5dZ0+O7KUKRiV3C21Ks5CDn3GGHRuYgWxhl7p0A9VxMx/h4jIuo2Jtn7qS7YrWeQzIr7QsfUWqVEMB70IyKkZt/B5jDrlz5v\n" +
                "xbogJW6slxKSxK64pPQfE8Gv/j9wPXkmoeK5XRru0s18RFzJw6mLuThy9pHUijmqFPLfmwlvqRXpAEQM1rmaiBF4POfvYuSisuO/rzPiUsOx5vgY/CPHW/gnGZqIQTnwn2Hcy7iMDacsUVic9481sJcMFxeXeba2tjbm5ubjBg0aNLh79+49OnTo0L5Zs2bN69WrV79WrVq1P/roo4+rVatW5d13333n\n" +
                "rbfe+m+Z/yHzX98yHf8i899l/leVKlX+9+233/7re++990GNGjVq1q1bt07Tpk0bt2vXrs3PMvr3799v1KhRo6fIcJLh6em5eJ2M3TJCZETJSEhIuJsjA/+Eg4blLJGRJuOmjPMyDsvYJsNbxsKFC93s7OzsJ02aNHHo0KHDevXq1Vv2vWPr1q1bNpTxpQzZ+0+qVq36/v/JkH39H5n/KfPfyvbaeN/5\n" +
                "2V94zTvvvPOu7H+1mjVr1vh/4FETIYyLnYcAAAAASUVORK5CYII=";
        ImageUtil.decodeImage(imgStr, "/Users/xingding/testdata/img/dest.jpg");


    }

}
