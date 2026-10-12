import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;

public final class da extends Canvas implements Runnable {
  public static da a_da;
  public short a_S;
  private boolean b_Z;
  public boolean a_Z;
  private long a_J;
  private long b_J;
  private long c_J;
  private long d_J;
  private short d_S;
  private boolean c_Z;
  private Thread a_Thread = null;
  public static byte a_B;
  av a_av;
  bs a_bs;
  public cd a_cd;
  static Random a_Random;
  public int[] a_AI;
  short b_S;
  short c_S;
  public Display a_Display;
  private String a_String;
  private boolean d_Z;
  private int a_I;

  public final void a(Displayable var1) {
    this.a_Display.setCurrent(var1);
  }

  protected da(Display var1) {
    this.a_Display = var1;
    this.setFullScreenMode(true);
    a_da = this;
    this.d_S = 16;
    this.b_Z = false;
    this.a_Z = false;
    if (a_Random == null) {
      (a_Random = new Random()).setSeed(System.currentTimeMillis());
    }

    this.b_S = (short)super.getWidth();
    this.c_S = (short)super.getHeight();
    if (this.b_S == 128) {
      this.c_S = 160;
    } else {
      if (this.b_S == 176 && this.c_S < 220) {
        this.c_S = 208;
      }
    }
  }

  public final int getWidth() {
    return this.b_S;
  }

  public final int getHeight() {
    return this.c_S;
  }

  public final void a() {
    this.b_Z = true;
    if (this.a_Thread == null && !this.c_Z) {
      this.a_Thread = new Thread(this);
      this.a_Thread.start();
      this.c_Z = true;
    }

    this.a_Display.setCurrent(this);
  }

  public final void b() {
    this.b_Z = false;
  }

  protected final void hideNotify() {
    System.out.println("hideNotify()");
    this.a_Z = true;
    if (Main.music != null) {
      Main.music.b();
    }

    if (this.a_cd != null) {
      cd.j();
    }

    Main.self.stopMusic();
    a_B = 7;
  }

  protected final void showNotify() {
    this.a_Z = false;
  }

  protected final void keyPressed(int var1) {
    if (!this.d_Z) {
      if (!bk.a_Z(var1 = b.a(var1))) {
        if (var1 == b.b_S) {
          if (at.a_String.equals("L6")
            || at.a_String.equals("L7")
            || at.a_String.equals("K1")
            || at.a_String.equals("E398")
            || at.a_String.equals("V8")
            || at.a_String.equals("E2")) {
            var1 = b.a_S;
          }
        } else if (var1 == b.a_S
          && (
            at.a_String.equals("L6")
              || at.a_String.equals("L7")
              || at.a_String.equals("K1")
              || at.a_String.equals("E398")
              || at.a_String.equals("V8")
              || at.a_String.equals("E2")
          )) {
          var1 = b.b_S;
        }

        synchronized (this) {
          switch (a_B) {
            case 1:
            case 5:
            default:
              break;
            case 2:
              if (this.a_av != null) {
                av var3 = this.a_av;
                if (this.a_av.a_B == 1) {
                  if (var1 == b.a_S) {
                    at.b_Z = true;
                    var3.a_Z = true;
                    o.g_B = at.d_B;
                    Main.self.showCover();
                  } else if (var1 == b.b_S) {
                    o.g_B = 0;
                    at.b_Z = false;
                    var3.a_Z = true;
                    Main.self.showCover();
                  }
                }
              }
              break;
            case 3:
              if (this.a_bs != null) {
                this.a_bs.a(var1);
              }
              break;
            case 4:
              if (this.a_cd != null) {
                this.a_cd.c(var1);
              }
              break;
            case 6:
              try {
                Main.self.destroyApp(true);
              } catch (MIDletStateChangeException var5) {
                var5.printStackTrace();
              }
              break;
            case 7:
              if (var1 == 8) {
                if (this.a_av != null) {
                  a_B = 2;
                } else if (this.a_bs != null) {
                  if (Main.music != null) {
                    Main.self.playMusic(ct.a_String, -1, o.g_B);
                    Main.music.b(o.g_B);
                    Main.music.a();
                  }

                  a_B = 3;
                } else if (this.a_cd != null) {
                  a_B = 4;
                  if (this.a_cd.a_bn != null) {
                    if (this.a_cd.a_bn.a_I == 24) {
                      bn.a_V(1);
                    } else if (this.a_cd.a_bn.a_I == 25) {
                      bn.a_V(2);
                    } else {
                      bn.a_V(0);
                    }
                  } else {
                    cd.m();
                  }
                }

                this.a_AI = null;
              }
          }
        }

        if (!cg.a_Z(this.a_AI, var1)) {
          this.a_AI = cg.a_AI(this.a_AI, var1);
        }

        if (at.d_Z && var1 == 42) {
          at.e_Z = !at.e_Z;
        }
      }
    }
  }

  protected final void keyReleased(int var1) {
    var1 = b.a(var1);
    if (cg.a_Z(this.a_AI, var1)) {
      this.a_AI = cg.b(this.a_AI, var1);
    }
  }

  public final void pointerPressed(int var1, int var2) {
    bk.a(var1, var2);
    if (a_B == 1) {
      if (at.a_String != null && (at.a_String.equals("L6") || at.a_String.equals("L7") || at.a_String.equals("E2"))) {
        if (var1 > a_da.getWidth() - 32 && var2 > a_da.getHeight() - 32) {
          at.b_Z = true;
          Main.self.showLogo();
          return;
        }

        if (var1 < 32 && var2 > a_da.getHeight() - 32) {
          at.b_Z = false;
          Main.self.showLogo();
          return;
        }
      } else {
        if (var1 > a_da.getWidth() - 32 && var2 > a_da.getHeight() - 32) {
          at.b_Z = false;
          Main.self.showLogo();
          return;
        }

        if (var1 < 32 && var2 > a_da.getHeight() - 32) {
          at.b_Z = true;
          Main.self.showLogo();
          return;
        }
      }
    } else if (a_B == 4) {
      if (this.a_cd != null) {
        this.a_cd.a(var1, var2);
        return;
      }
    } else if (a_B == 3) {
      if (this.a_bs != null) {
        this.a_bs.a(var1, var2);
        return;
      }
    } else {
      if (a_B == 6) {
        try {
          Main.self.destroyApp(true);
          return;
        } catch (MIDletStateChangeException var4) {
          var4.printStackTrace();
          return;
        }
      }

      if (a_B == 2 && this.a_av != null) {
        av var5 = this.a_av;
        cm var7;
        if ((var7 = i.a(var1, var2)) != null) {
          if (var7.a_B == 2) {
            if (var5.a_B == 1) {
              at.b_Z = false;
              o.g_B = 0;
              var5.a_Z = true;
              Main.self.showCover();
              return;
            }
          } else if (var7.a_B == 1 && var5.a_B == 1) {
            at.b_Z = true;
            var5.a_Z = true;
            o.g_B = at.d_B;
            Main.self.showCover();
          }
        }
      }
    }
  }

  public final void pointerDragged(int var1, int var2) {
    if (a_B == 4 && this.a_cd != null) {
      this.a_cd.c(var1, var2);
    }
  }

  public final void pointerReleased(int var1, int var2) {
    if (a_B == 4 && this.a_cd != null) {
      this.a_cd.b(var1, var2);
    }
  }

  public final void run() {
    while (this.b_Z) {
      if (!this.a_Z) {
        this.a_J = System.currentTimeMillis();
        this.repaint();
        this.serviceRepaints();
        this.a_S++;
        if (this.a_S >= 127) {
          this.a_S = 0;
        }

        this.b_J = System.currentTimeMillis();
        if (at.a_String != null && (at.a_String.equals("L6") || at.a_String.equals("L7"))) {
          try {
            Thread.sleep(10L);
          } catch (InterruptedException var3) {
            var3.printStackTrace();
          }
        } else if (this.b_J - this.a_J < (long)this.d_S) {
          try {
            Thread.sleep((long)this.d_S - (this.b_J - this.a_J));
          } catch (InterruptedException var2) {
            var2.printStackTrace();
          }
        }

        this.c_J = System.currentTimeMillis();
        if (this.c_J > this.a_J) {
          this.d_J = 1000L / (this.c_J - this.a_J);
        }
      } else {
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException var4) {
          var4.printStackTrace();
        }
      }
    }

    this.c_Z = false;
  }

  public final void a(String var1, int var2) {
    this.a_String = var1;
    this.a_I = var2;
    this.d_Z = true;
  }

  protected final void paint(Graphics var1) {
    synchronized (this) {
      var1.setFont(cg.a_Font);
      switch (a_B) {
        case 0:
          var1.setColor(0);
          var1.fillRect(0, 0, a_da.getWidth(), a_da.getHeight());
          break;
        case 1:
          var1.setColor(0);
          var1.fillRect(0, 0, a_da.getWidth(), a_da.getHeight());
          cg.a(var1, "Bật âm nhạc?", a_da.getWidth() / 2, a_da.getHeight() / 2, 13200659, 16777215, 33);
          var1.setColor(16777215);
          if (at.a_String == null || !at.a_String.equals("L6") && !at.a_String.equals("L7") && !at.a_String.equals("E2")) {
            vh.drawString(var1, "Không", a_da.getWidth() - 1, a_da.getHeight() - 1, 40);
            vh.drawString(var1, "Có", 1, a_da.getHeight() - 1, 36);
          } else {
            vh.drawString(var1, "Có", a_da.getWidth() - 1, a_da.getHeight() - 1, 40);
            vh.drawString(var1, "Không", 1, a_da.getHeight() - 1, 36);
          }
          break;
        case 2:
          if (this.a_av != null) {
            this.a_av.a(var1);
          }
          break;
        case 3:
          if (this.a_bs != null) {
            this.a_bs.a(var1);
          }
          break;
        case 4:
          if (this.a_cd != null) {
            try {
              this.a_cd.a(var1);
            } catch (Exception var9) {
              var9.printStackTrace();
            }
          }
        case 5:
        default:
          break;
        case 6:
          var1.setColor(16711680);
          vh.drawString(var1, "Hãy chạy trên điện thoại", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
          break;
        case 7:
          var1.setColor(0);
          var1.fillRect(0, 0, a_da.getWidth(), a_da.getHeight());
          var1.setColor(16777215);
          vh.drawString(var1, "Nhấn OK để tiếp tục", at.a_AS[0] / 2, at.a_AS[1] / 2, 17);
      }

      Graphics var4 = var1;
      var1.translate(0, 0);
      if (this.d_Z) {
        if (this.a_I <= 0) {
          this.a_String = null;
          this.d_Z = false;
        } else {
          if (this.a_String != null && !this.a_String.equals("")) {
            String[] var8 = cg.a(this.a_String, at.a_AS[0] - 32);
            int var5 = at.a_AS[0] / 2 - var8.length * cg.b_S / 2 - 5;
            int var6 = at.a_AS[0] - 24;
            int var7 = var8.length * cg.b_S + 10;
            var1.setColor(1381651);
            var1.fillRoundRect(11, var5 - 1, var6 + 3, var7 + 3, 8, 8);
            var1.setColor(16053217);
            var1.drawRoundRect(12, var5, var6, var7, 8, 8);
            var1.setColor(6768205);
            var1.fillRect(14, var5 + 2, var6 - 3, var7 - 3);

            for (int var11 = 0; var11 < var8.length; var11++) {
              var4.setColor(16777215);
              vh.drawString(var4, var8[var11], at.a_AS[0] / 2, at.a_AS[0] / 2 - var8.length * cg.b_S / 2 + var11 * cg.b_S, 17);
            }
          }

          this.a_I--;
        }
      }

      var1.setClip(0, 0, a_da.getWidth(), a_da.getHeight());
      if (at.e_Z) {
        var1.setColor(16711680);
        vh.drawString(
          var1, "Bộ nhớ:" + (Runtime.getRuntime().freeMemory() >> 10) + "/" + (Runtime.getRuntime().totalMemory() >> 10), 2, a_da.getHeight() - 2, 36
        );
        vh.drawString(var1, "FPS:" + a_da.d_J, 2, a_da.getHeight() - cg.b_S - 2, 36);
      }
    }

    bk.a(var1);
  }
}
