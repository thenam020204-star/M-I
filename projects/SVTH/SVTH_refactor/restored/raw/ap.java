import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public final class ap {
  private s a_s;
  private volatile boolean a_Z = false;
  private static boolean b_Z;
  private Vector a_Vector;
  private final int a_I;
  private u a_u;
  private int[] a_AI = new int[0];

  public ap(u var1) {
    this.a_I = 20;
    this.a_Vector = new Vector(20);
    this.a_u = var1;
  }

  public final void a(af var1) {
    synchronized (this.a_AI) {
      if (this.a_Vector.size() < this.a_I) {
        this.a_Vector.addElement(var1);
        this.a_AI.notifyAll();
      }
    }
  }

  public final void a() {
    if (!this.a_Z) {
      this.a_Z = true;
      this.a_s = new s(this);
      this.a_s.setPriority(5);
      this.a_s.start();
    }
  }

  public final void b() {
    this.a_Z = false;
    synchronized (this.a_AI) {
      this.a_Vector.removeAllElements();
      this.a_AI.notifyAll();
    }
  }

  public final void c() {
    synchronized (this.a_AI) {
      this.a_Vector.removeAllElements();
      this.a_AI.notifyAll();
    }

    this.a_s.a_Z = true;
  }

  static boolean a_Z(ap var0) {
    return var0.a_Z;
  }

  static int[] a_AI(ap var0) {
    return var0.a_AI;
  }

  static Vector a_Vector(ap var0) {
    return var0.a_Vector;
  }

  // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
  // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
  static void a(ap var0, af var1, s var2) {
    var2 = var2;
    af var104 = var1;
    var0 = var0;
    af var3 = new af(null, null, null);
    p var4 = var1.a_p();
    HttpConnection var5 = null;
    DataInputStream var6 = null;
    OutputStream var7 = null;
    ByteArrayOutputStream var8 = null;
    boolean var9 = false;
    boolean var10 = b_Z;
    boolean var11 = false;
    boolean var60 = false /* VF: Semaphore variable */;

    label605: {
      label606: {
        label607: {
          label608: {
            label609: {
              label610: {
                label611: {
                  try {
                    var60 = true;
                    int var13 = 0;

                    while (true) {
                      String var16 = var104.b();
                      HttpConnection var112;
                      if (var10) {
                        int var15 = "http://".length();
                        String var17;
                        int var18;
                        String var111;
                        if ((var18 = var16.indexOf(47, var15)) < 0) {
                          var17 = var16.substring(var15);
                          var111 = "";
                        } else {
                          var17 = var16.substring(var15, var18);
                          var111 = var16.substring(var18);
                        }

                        (var112 = (HttpConnection)Connector.open("http://10.0.0.172:80" + var111)).setRequestProperty("X-Online-Host", var17);
                      } else {
                        var112 = (HttpConnection)Connector.open(var16);
                      }

                      var112.setRequestMethod(var104.a_String());
                      var112.setRequestProperty("Q-UA", var0.a_u.a_String());
                      var112.setRequestProperty("screen", var0.a_u.b());
                      var112.setRequestProperty("bigchar", var0.a_u.c());
                      var112.setRequestProperty("midchar", var0.a_u.d());
                      var112.setRequestProperty("smlchar", var0.a_u.e());
                      var5 = var112;
                      if (var104.a_String().equals("POST")) {
                        byte[] var12 = var104.a_AB();
                        var112.setRequestProperty("Content-Length", String.valueOf(var12.length));
                        (var7 = var112.openOutputStream()).write(var12);
                      }

                      int var107;
                      try {
                        if (var0.a_u.a_String().indexOf("wtk") > 0) {
                          Thread.sleep(3000L);
                        }

                        var107 = var5.getResponseCode();
                        if (var2.a_Z) {
                          var60 = false;
                          break;
                        }
                      } catch (Throwable var95) {
                        if (var11) {
                          throw var95;
                        }

                        var10 = !var10;
                        var11 = true;

                        try {
                          if (var7 != null) {
                            var7.close();
                          }

                          var5.close();
                          Thread.sleep(80L);
                        } catch (Throwable var93) {
                        }
                        continue;
                      }

                      if (var107 != 302 && var107 != 301 || var13 >= 16) {
                        if (var107 != 200 && var107 != 206) {
                          var9 = true;
                          new StringBuffer(String.valueOf(var107)).append(" ").append(var112.getResponseMessage()).toString();
                          var3.c("Kết nối mạng gặp lỗi không rõ‚ vui lòng thử lại sau!");
                          var60 = false;
                          break label606;
                        }

                        var6 = var112.openDataInputStream();
                        boolean var109 = false;
                        var8 = new ByteArrayOutputStream();
                        byte[] var106 = new byte[1024];

                        while (true) {
                          if ((var13 = var6.read(var106)) > 0) {
                            var104.a_J += (long)var13;
                            if (var104.a_J < 409600L) {
                              if (!var0.a_Z || var2.a_Z) {
                                throw new IOException("HttpCommunicator closed or msg caceled!");
                              }

                              var8.write(var106, 0, var13);
                              continue;
                            }

                            var109 = true;
                          }

                          var8.flush();
                          byte[] var103 = var8.toByteArray();
                          if (var109) {
                            var9 = true;
                            var60 = false;
                          } else {
                            var3.a(var103);
                            var60 = false;
                          }
                          break label606;
                        }
                      }

                      String var108;
                      if ((var108 = var112.getHeaderField("Location")) == null) {
                        throw new IOException("Redirect failed!");
                      }

                      try {
                        if (var7 != null) {
                          var7.close();
                        }

                        var5.close();
                        Thread.sleep(80L);
                      } catch (Throwable var92) {
                      }

                      var104.a(var108);
                      var104.b("GET");
                      var11 = true;
                      var13++;
                    }
                  } catch (IllegalArgumentException var96) {
                    var9 = true;
                    new StringBuffer("IllegalArgumentException: ").append(var96.getMessage()).toString();
                    var3.c("Kết nối mạng gặp lỗi bất hợp lệ‚ hãy kiểm tra cài đặt mạng rồi thử lại!");
                    var60 = false;
                    break label611;
                  } catch (ConnectionNotFoundException var97) {
                    var9 = true;
                    new StringBuffer("ConnectionNotFoundException: ").append(var97.getMessage()).toString();
                    var3.c("Không tìm thấy kết nối mạng chỉ định‚ vui lòng thử lại sau!");
                    var60 = false;
                    break label610;
                  } catch (IOException var98) {
                    if (!var2.a_Z) {
                      var9 = true;
                      new StringBuffer("IOException: ").append(var98.getMessage()).toString();
                      var3.c("Hết thời gian kết nối‚ vui lòng thử lại.");
                      var60 = false;
                    } else {
                      var60 = false;
                    }
                    break label609;
                  } catch (SecurityException var99) {
                    var9 = true;
                    new StringBuffer("SecurityException: ").append(var99.getMessage()).toString();
                    var3.c("Lỗi kết nối‚ vui lòng thử lại.");
                    var60 = false;
                    break label608;
                  } catch (Throwable var100) {
                    var9 = true;
                    new StringBuffer("Throwable: ").append(var100.getMessage()).toString();
                    var3.c("Cổng kết nối gặp lỗi không rõ‚ vui lòng kiểm tra rồi thử lại!");
                    var60 = false;
                    break label607;
                  } finally {
                    if (var60) {
                      if (var8 != null) {
                        try {
                          var8.close();
                        } catch (IOException var64) {
                        }
                      }

                      if (var6 != null) {
                        try {
                          var6.close();
                        } catch (IOException var63) {
                        }
                      }

                      if (var7 != null) {
                        try {
                          var7.close();
                        } catch (IOException var62) {
                        }
                      }

                      if (var5 != null) {
                        try {
                          var5.close();
                        } catch (IOException var61) {
                        }
                      }
                    }
                  }

                  if (var7 != null) {
                    try {
                      var7.close();
                    } catch (IOException var65) {
                    }
                  }

                  if (var5 != null) {
                    try {
                      var5.close();
                      return;
                    } catch (IOException var94) {
                    }
                  }

                  return;
                }

                if (var8 != null) {
                  try {
                    var8.close();
                  } catch (IOException var91) {
                  }
                }

                if (var6 != null) {
                  try {
                    var6.close();
                  } catch (IOException var90) {
                  }
                }

                if (var7 != null) {
                  try {
                    var7.close();
                  } catch (IOException var89) {
                  }
                }

                if (var5 != null) {
                  try {
                    var5.close();
                  } catch (IOException var88) {
                  }
                }
                break label605;
              }

              if (var8 != null) {
                try {
                  var8.close();
                } catch (IOException var87) {
                }
              }

              if (var6 != null) {
                try {
                  var6.close();
                } catch (IOException var86) {
                }
              }

              if (var7 != null) {
                try {
                  var7.close();
                } catch (IOException var85) {
                }
              }

              if (var5 != null) {
                try {
                  var5.close();
                } catch (IOException var84) {
                }
              }
              break label605;
            }

            if (var8 != null) {
              try {
                var8.close();
              } catch (IOException var83) {
              }
            }

            if (var6 != null) {
              try {
                var6.close();
              } catch (IOException var82) {
              }
            }

            if (var7 != null) {
              try {
                var7.close();
              } catch (IOException var81) {
              }
            }

            if (var5 != null) {
              try {
                var5.close();
              } catch (IOException var80) {
              }
            }
            break label605;
          }

          if (var8 != null) {
            try {
              var8.close();
            } catch (IOException var79) {
            }
          }

          if (var6 != null) {
            try {
              var6.close();
            } catch (IOException var78) {
            }
          }

          if (var7 != null) {
            try {
              var7.close();
            } catch (IOException var77) {
            }
          }

          if (var5 != null) {
            try {
              var5.close();
            } catch (IOException var76) {
            }
          }
          break label605;
        }

        if (var8 != null) {
          try {
            var8.close();
          } catch (IOException var75) {
          }
        }

        if (var6 != null) {
          try {
            var6.close();
          } catch (IOException var74) {
          }
        }

        if (var7 != null) {
          try {
            var7.close();
          } catch (IOException var73) {
          }
        }

        if (var5 != null) {
          try {
            var5.close();
          } catch (IOException var72) {
          }
        }
        break label605;
      }

      if (var8 != null) {
        try {
          var8.close();
        } catch (IOException var71) {
        }
      }

      if (var6 != null) {
        try {
          var6.close();
        } catch (IOException var70) {
        }
      }

      if (var7 != null) {
        try {
          var7.close();
        } catch (IOException var69) {
        }
      }

      if (var5 != null) {
        try {
          var5.close();
        } catch (IOException var68) {
        }
      }
    }

    if (var9) {
      try {
        var4.b(var3);
      } catch (Throwable var66) {
        var66.printStackTrace();
      }
    } else {
      if (b_Z != var10) {
        b_Z = var10;
      }

      try {
        if (!var2.a_Z) {
          var4.a(var3);
        }
      } catch (Throwable var67) {
        var67.printStackTrace();
      }
    }
  }
}
