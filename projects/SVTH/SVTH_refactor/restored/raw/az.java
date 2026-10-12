import java.io.ByteArrayOutputStream;

public final class az {
  private static byte[] a_AB = new byte[]{
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    62,
    -1,
    -1,
    -1,
    63,
    52,
    53,
    54,
    55,
    56,
    57,
    58,
    59,
    60,
    61,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    10,
    11,
    12,
    13,
    14,
    15,
    16,
    17,
    18,
    19,
    20,
    21,
    22,
    23,
    24,
    25,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    26,
    27,
    28,
    29,
    30,
    31,
    32,
    33,
    34,
    35,
    36,
    37,
    38,
    39,
    40,
    41,
    42,
    43,
    44,
    45,
    46,
    47,
    48,
    49,
    50,
    51,
    -1,
    -1,
    -1,
    -1,
    -1
  };

  public static byte[] a(String var0) {
    byte[] var6;
    int var1 = (var6 = var0.getBytes()).length;
    ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1);
    int var3 = 0;

    label67:
    while (var3 < var1) {
      byte var4 = a_AB[var6[var3++]];
      if (var3 >= var1 || var4 != -1) {
        if (var4 == -1) {
          break;
        }

        byte var5;
        do {
          var5 = a_AB[var6[var3++]];
        } while (var3 < var1 && var5 == -1);

        if (var5 == -1) {
          break;
        }

        var2.write(var4 << 2 | (var5 & 48) >>> 4);

        while ((var4 = var6[var3++]) != 61) {
          var4 = a_AB[var4];
          if (var3 >= var1 || var4 != -1) {
            if (var4 == -1) {
              return var2.toByteArray();
            }

            var2.write((var5 & 15) << 4 | (var4 & 60) >>> 2);

            while ((var5 = var6[var3++]) != 61) {
              var5 = a_AB[var5];
              if (var3 >= var1 || var5 != -1) {
                if (var5 == -1) {
                  return var2.toByteArray();
                }

                var2.write((var4 & 3) << 6 | var5);
                continue label67;
              }
            }

            return var2.toByteArray();
          }
        }

        return var2.toByteArray();
      }
    }

    return var2.toByteArray();
  }
}
