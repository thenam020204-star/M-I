import java.util.Vector;

public final class ci {
  public boolean a_Z;
  public bz[] a_Abz;
  private bz c_bz;
  private bz d_bz;
  bz a_bz;
  private boolean e_Z;
  public boolean b_Z;
  bz b_bz;
  public byte a_B;
  private short a_S;
  private short b_S;
  private boolean f_Z;
  private byte b_B;
  public boolean c_Z;
  public boolean d_Z;
  private short c_S;
  private short d_S = -1;
  private boolean g_Z;
  private short[] a_AS;
  private short e_S;
  private String[] a_AString;
  public static int a_I;
  private short f_S;
  private short[][] a_AAS = new short[][]{{37, 38, 39, 40, 41, 42, 43, 44}, {40, 41, 42, 43, 44, 45, 46, 47}, {42, 43, 44, 45, 46, 47, 48, 49, 50}};

  private void b() {
    if (this.a_AString != null) {
      short var1 = -1;

      for (int var2 = this.a_AString.length - 1; var2 > 0; var2 -= 2) {
        if (cg.a_I(this.a_AString[var2 - 1]) == this.e_S) {
          var1 = (short)(var2 - 1);
        }
      }

      if (var1 > 0) {
        String[] var3 = new String[var1];
        System.arraycopy(this.a_AString, 0, var3, 0, var3.length);
        this.a_AString = var3;
      } else {
        if (var1 == 0) {
          this.a_AString = null;
        }
      }
    }
  }

  private void a(boolean var1) {
    if (this.a_AS == null) {
      this.a_AS = new short[2];
      this.a_AS[0] = this.e_S;
      this.a_AS[1] = (short)(var1 ? 1 : 0);
    } else {
      for (byte var2 = 0; var2 < this.a_AS.length; var2 += 2) {
        if (this.a_AS[var2] == this.e_S) {
          this.a_AS[var2 + 1] = (short)(var1 ? 1 : 0);
          return;
        }
      }

      short[] var3 = new short[this.a_AS.length + 2];
      System.arraycopy(this.a_AS, 0, var3, 0, this.a_AS.length);
      this.a_AS = var3;
      var3[var3.length - 2] = this.e_S;
      var3[var3.length - 1] = (short)(var1 ? 1 : 0);
    }
  }

  private void c() {
    if (this.a_AS != null) {
      short var1 = -1;

      for (int var2 = this.a_AS.length - 1; var2 > 0; var2 -= 2) {
        if (this.a_AS[var2 - 1] == this.e_S) {
          var1 = (short)(var2 - 1);
        }
      }

      if (var1 > 0) {
        short[] var3 = new short[var1];
        System.arraycopy(this.a_AS, 0, var3, 0, var3.length);
        this.a_AS = var3;
      } else {
        if (var1 == 0) {
          this.a_AS = null;
        }
      }
    }
  }

  private boolean a_Z() {
    for (byte var1 = 0; this.a_AS != null && var1 < this.a_AS.length - 1; var1 += 2) {
      if (this.a_AS[var1] <= this.e_S && this.a_AS[var1 + 1] == 0) {
        return false;
      }
    }

    return true;
  }

  public final void a(String var1, int var2) {
    this.a_Abz = null;
    var1 = cg.a_String(var1);
    Vector var3 = new Vector();
    short var4 = cg.a_S(var1, "eventAmount");

    for (int var5 = 0; var1 != null && var5 < var4; var5++) {
      String var6;
      if ((var6 = cg.a(var1, "event" + (var5 + 1) + ":", "event" + (var5 + 1) + "End")) != null) {
        short var7 = cg.a_S(var6, "id");
        cg.a_B(var6, "type");
        String var8 = cg.a_String(var6, "name");
        short[] var9 = cg.a_AS(var6, "area");
        boolean var10 = cg.b_Z(var6, "auto");
        boolean var11 = cg.b_Z(var6, "loop");
        String[][] var12 = cg.a_AAString(var6, "script:", "scriptEnd", null);
        int[] var19 = cg.a_AI(var6, "phase");
        if (!o.b_Z(var7) && (var19 == null || cg.a_Z(var19, var2)) && var9 != null) {
          bz var20;
          (var20 = new bz()).a_S = var7;
          var20.a_String = var8;
          var20.a_AS = var9;
          var20.a_Z = var10;
          var20.b_Z = var11;
          var20.a_AAString = var12;
          var3.addElement(var20);
        }
      }
    }

    if (o.c_AAS != null && o.b_S == 67) {
      o.a_V(6701);
      bz var15;
      (var15 = new bz()).a_S = 6701;
      var15.a_String = "副本战";
      var15.a_AS = new short[]{(short)(o.q_AS[0] - 80), (short)(o.q_AS[1] - 24), 160, 48};
      var15.a_Z = true;
      var15.b_Z = false;
      String[] var21 = new String[]{"副本战斗"};

      for (int var25 = 0; o.c_AAS != null && var25 < o.c_AAS.length; var25++) {
        if (o.c_AAS[var25][1] == -1) {
          var21 = cg.a(var21, o.c_AAS[var25][0] + "|" + (o.a_Abl[0].a_AI[0] + 5) + "|1");
        } else {
          var21 = cg.a(var21, o.c_AAS[var25][0] + "|" + o.c_AAS[var25][1] + "|1");
        }
      }

      var15.a_AAString = new String[][]{{"对话", "Đã vào đây rồi thì ở lại luôn đi!"}, var21};
      var3.addElement(var15);
    }

    if (o.k_AString != null) {
      if (o.h_B == 1) {
        short[] var16 = cg.b_AS(o.k_AString[1], ",");
        bz var22;
        (var22 = new bz()).a_String = "换场景2";
        var22.a_AS = new short[]{(short)(var16[0] - 10), (short)(var16[1] - 10), 20, 20};
        var22.a_Z = true;
        var22.b_Z = false;
        if (o.j_B >= o.i_B) {
          var22.a_AAString = new String[][]{{"换场景", o.m_AString[1], "1"}};
        } else {
          var22.a_AAString = new String[][]{{"换场景", String.valueOf(o.b_S), "1"}};
        }

        var3.addElement(var22);
      } else {
        for (int var23 = 0; var23 < o.k_AString.length; var23++) {
          short[] var17 = cg.b_AS(o.k_AString[var23], ",");
          bz var26;
          (var26 = new bz()).a_String = "换场景" + (var23 + 1);
          var26.a_AS = new short[]{(short)(var17[0] - 10), (short)(var17[1] - 10), 20, 20};
          var26.a_Z = true;
          var26.b_Z = true;
          if (o.j_B >= o.i_B && var23 == 1) {
            var26.a_AAString = new String[][]{{"换场景", o.m_AString[1], "1"}};
          } else if (o.j_B == 1 && var23 == 0) {
            var26.a_AAString = new String[][]{{"换场景", o.m_AString[0], "1"}};
          } else {
            if (var23 == 0) {
              var26.a_AAString = new String[][]{{"换场景2", String.valueOf(o.b_S), "1"}};
            }

            if (var23 == 1) {
              var26.a_AAString = new String[][]{{"换场景", String.valueOf(o.b_S), "1"}};
            }
          }

          var3.addElement(var26);
        }
      }
    }

    if (o.q_String == null) {
      o.q_String = cg.a_String("/bin/sceneChange.bin");
    }

    short[][] var18;
    if ((var18 = cg.a_AAS(o.q_String, "S" + o.b_S + "A:", "S" + o.b_S + "AEnd", "=")) != null) {
      short[][] var24 = cg.a_AAS(o.q_String, "Connect:", "ConnectEnd", "=");
      int var27 = -1;

      for (int var14 = 0; var14 < var18.length; var14++) {
        if (var27 < 0) {
          for (int var28 = 0; var28 < var24.length; var28++) {
            if (var24[var28][0] == o.b_S) {
              var27 = var28;
              break;
            }
          }
        }

        bz var29;
        (var29 = new bz()).a_String = "换场景" + (var14 + 1);
        var29.a_AS = var18[var14];
        var29.a_Z = true;
        var29.b_Z = true;
        var29.a_AAString = new String[][]{{"换场景", String.valueOf(var24[var27 + var14][2]), String.valueOf(var24[var27 + var14][3])}};
        var3.addElement(var29);
      }
    }

    if (var3.size() > 0) {
      this.a_Abz = new bz[var3.size()];
      var3.copyInto(this.a_Abz);
    }
  }

  public final void a(short[] var1) {
    if (!this.a_Z) {
      if (da.a_da.a_cd.b_B == 1 && !da.a_da.a_cd.a_bb.a_Z() && o.a_Abl[0] != null) {
        bz var10001;
        short[] var2 = var1;
        ci var4 = this;
        label64:
        if (!this.a_Z && var1 != null) {
          for (int var3 = 0; var4.a_Abz != null && var3 < var4.a_Abz.length; var3++) {
            if (cg.a(var4.a_Abz[var3].a_AS[0], var4.a_Abz[var3].a_AS[1], var4.a_Abz[var3].a_AS[2], var4.a_Abz[var3].a_AS[3], var2[0], var2[1], var2[2], var2[3])
              && var4.a_Abz[var3].a_Z
              && !o.b_Z(var4.a_Abz[var3].a_S)) {
              var10001 = var4.a_Abz[var3];
              break label64;
            }
          }

          var10001 = null;
        } else {
          var10001 = null;
        }

        this.d_bz = var10001;
        if (this.d_bz != null) {
          if (this.e_Z) {
            this.c_bz = this.d_bz;
          }

          if (this.c_bz == null) {
            this.c_bz = this.d_bz;
            if (this.c_bz.a_Z) {
              if (at.e_Z) {
                System.out.println("执行事件:" + this.c_bz.a_String);
              }

              this.b(this.c_bz);
              return;
            }
          } else if (this.c_bz != this.d_bz) {
            this.c_bz = null;
            this.c_bz = this.d_bz;
            if (this.c_bz.a_Z) {
              if (at.e_Z) {
                System.out.println("执行事件:" + this.c_bz.a_String);
              }

              this.b(this.c_bz);
              return;
            }
          }
        } else {
          this.c_bz = null;
          if (this.e_Z) {
            this.e_Z = false;
          }
        }
      }
    }
  }

  private void b(bz var1) {
    if (!this.a_Z) {
      if (var1.a_Z) {
        this.a(var1);
      }
    }
  }

  public final void a(bz var1) {
    if (!this.a_Z && da.a_da.a_cd.a_bn == null) {
      this.a_Z = true;
      this.a_bz = var1;
      this.c_S = 0;
      this.d_S = -1;
      da.a_da.a_cd.b_String = null;
      this.a_B = -1;

      for (int var2 = 0; var2 < o.a_Abl.length; var2++) {
        o.a_Abl[var2].c_V();
      }

      this.f_Z = true;
      this.b_B = 0;
      this.a_S = 0;
      this.b_S = 0;
      this.d_Z = true;
      da.a_da.a_cd.g_Z = false;
    }
  }

  private void d() {
    this.d_Z = false;
    this.a_bz = null;
    this.a_Z = false;
    if (da.a_da.a_cd != null) {
      da.a_da.a_cd.g_Z = true;
    }
  }

  public final void a_V() {
    if (this.d_Z) {
      for (int var1 = 0; this.a_bz != null && this.a_bz.a_AAString != null && var1 < this.a_bz.a_AAString.length; var1++) {
        if (var1 == this.c_S && this.c_S > this.d_S) {
          this.d_S = this.c_S;
          if (this.a_S > 0) {
            this.a_S--;
          }

          if (!this.g_Z) {
            if (!this.a_bz.a_AAString[var1][0].startsWith("/*")) {
              this.a(this.a_bz.a_AAString[var1], 0);
              continue;
            }

            this.g_Z = true;
          } else if (this.a_bz.a_AAString[var1][0].startsWith("*/")) {
            this.g_Z = false;
          }

          this.c_S++;
        }
      }

      if (this.a_bz != null && this.a_bz.a_AAString != null && this.c_S > this.a_bz.a_AAString.length - 1) {
        if (this.a_bz != null && !this.a_bz.b_Z && this.a_bz.a_S > 0) {
          o.b_V(this.a_bz.a_S);
        }

        this.d();
      }
    }
  }

  private void a(String[] var1, int var2) {
    if (!var1[var2].equals("换场景2") && !var1[var2].equals("换场景") && o.a_Abl[0].b_Z) {
      o.a_Abl[0].j_V();
    }

    if (var1[var2].equals("}")) {
      this.c();
      this.b();
      if (this.e_S > 0) {
        this.e_S--;
      }

      this.a(0);
    } else if (var1[var2].equals("{")) {
      this.e_S++;
      this.a(this.f_Z);
      this.a(0);
    } else if (!this.a_Z()) {
      this.a(0);
    } else {
      if (at.e_Z) {
        System.out.println("执行事件:" + var1[var2]);
      }

      if (var1[var2].equals("对话")) {
        da.a_da.a_cd.a(var1, var2 + 1);
        this.a(1);
      } else if (var1[var2].equals("必败战斗")) {
        this.c_Z = true;
        da.a_da.a_cd.b(var1, var2 + 1);
      } else if (var1[var2].equals("教学战斗")) {
        da.a_da.a_cd.r_Z = true;
        da.a_da.a_cd.b(var1, var2 + 1);
      } else if (var1[var2].equals("战斗")) {
        da.a_da.a_cd.q_Z = true;
        da.a_da.a_cd.b(var1, var2 + 1);
      } else if (var1[var2].equals("副本战斗")) {
        da.a_da.a_cd.p_Z = true;
        da.a_da.a_cd.b(var1, var2 + 1);
      } else if (var1[var2].equals("清除BOSS") || var1[var2].equals("清除敌人")) {
        da.a_da.a_cd.d(cg.a_I(var1[var2 + 1]));
      } else if (var1[var2].equals("复活角色")) {
        da.a_da.a_cd.b_V(cg.a_I(var1[var2 + 1]));
      } else {
        label1768: {
          label1887: {
            if (var1[var2].equals("增加阶段")) {
              o.a_B++;
            } else if (var1[var2].equals("更新阶段")) {
              o.a_B = cg.a_B(var1[var2 + 1]);
            } else if (var1[var2].equals("去除事件")) {
              o.b_V(cg.a_I(var1[var2 + 1]));
            } else if (var1[var2].equals("恢复事件")) {
              o.a_V(cg.a_I(var1[var2 + 1]));
            } else if (var1[var2].equals("获取道具")) {
              o.b(cg.a_I(var1[var2 + 1]), cg.a_I(var1[var2 + 2]));
            } else if (var1[var2].equals("获取装备")) {
              o.d(cg.a_I(var1[var2 + 1]), -1);
            } else if (var1[var2].equals("获取技能")) {
              o.a(o.a_Abl[0], cg.a_I(var1[var2 + 1]), cg.a_I(var1[var2 + 2]));
            } else {
              if (var1[var2].equals("提示信息")) {
                da.a_da.a_cd.a_AString = new String[]{var1[var2 + 1]};
                da.a_da.a_cd.d_B = 0;
                da.a_da.a_cd.b_B = 3;
                return;
              }

              if (var1[var2].equals("短信购买")) {
                if (cg.a_I(var1[var2 + 1]) == 99) {
                  if (cc.a("/pic/order.jpg") != null) {
                    da.a_da.a_cd.b_B = 10;
                    return;
                  }

                  df.a("/bin/smsmenu.bin");
                  da.a_da.a_cd.a_df.b(6);
                }

                return;
              }

              if (var1[var2].equals("失败处理")) {
                o.g();
              } else {
                if (var1[var2].equals("换场景2") || var1[var2].equals("换场景")) {
                  if (o.d_Z) {
                    if (var1[var2].equals("换场景2")) {
                      da.a_da.a_cd.o_Z = true;
                      o.j_B--;
                    } else {
                      o.j_B++;
                    }
                  }

                  da.a_da.a_cd.g_Z = true;
                  cd.c_S = (short)cg.a_B(var1[var2 + 2]);
                  da.a_da.a_cd.a_V(cg.a_S(var1[var2 + 1]));
                  return;
                }

                if (var1[var2].equals("瞬移")) {
                  short var48 = cg.a_S(var1[var2 + 1]);
                  short var75 = cg.a_S(var1[var2 + 2]);
                  this.e_Z = true;

                  for (int var92 = 0; o.a_Abl != null && var92 < o.a_Abl.length; var92++) {
                    o.a_Abl[var92].d_S = var48;
                    if (var92 == 0) {
                      o.a_Abl[var92].e_S = var75;
                    } else {
                      o.a_Abl[var92].e_S = (short)(o.a_Abl[var92 - 1].e_S - o.a_Abl[0].c_B);
                    }

                    o.a_Abl[var92].a_V(0);
                    o.a_Abl[0].d_V();
                  }

                  this.a(0);
                  return;
                }

                if (var1[var2].equals("转向")) {
                  o.a_Abl[0].a_V(cg.a_I(var1[var2 + 1]));
                  o.a_Abl[0].c_V();
                } else {
                  if (var1[var2].equals("NPC转向")) {
                    byte var47 = cg.a_B(var1[var2 + 1]);
                    byte var74 = cg.a_B(var1[var2 + 2]);

                    for (int var91 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var91 < da.a_da.a_cd.a_bj.a_Acz.length; var91++) {
                      if (da.a_da.a_cd.a_bj.a_Acz[var91] != null
                        && da.a_da.a_cd.a_bj.a_Acz[var91].i_B == 1
                        && ((bl)da.a_da.a_cd.a_bj.a_Acz[var91]).h_S == var47) {
                        ((bl)da.a_da.a_cd.a_bj.a_Acz[var91]).a_V(var74);
                        ((bl)da.a_da.a_cd.a_bj.a_Acz[var91]).c_V();
                      }
                    }

                    this.a(0);
                    return;
                  }

                  if (var1[var2].equals("绘画章节")) {
                    da.a_da.a_cd.a(cg.a_I(var1[var2 + 1]), cg.a_I(var1[var2 + 2]), cg.a_I(var1[var2 + 3]), cg.a_I(var1[var2 + 4]), cg.a_I(var1[var2 + 5]));
                    this.a(1);
                    return;
                  }

                  if (var1[var2].equals("特写动画")) {
                    da.a_da.a_cd.a(String.valueOf(var1[var2 + 1]), cg.a_I(var1[var2 + 2]), cg.a_I(var1[var2 + 3]), cg.a_I(var1[var2 + 4]));
                    this.a(1);
                    return;
                  }

                  if (var1[var2].equals("切换焦点")) {
                    da.a_da.a_cd.d(cg.a_I(var1[var2 + 1]), cg.a_I(var1[var2 + 2]));
                    this.a(1);
                    return;
                  }

                  if (var1[var2].equals("还原焦点")) {
                    da.a_da.a_cd.t_Z = true;
                    return;
                  }

                  if (var1[var2].equals("走动")) {
                    short var46 = o.e_S;
                    int var73 = cg.a_I(var1[var2 + 1]);
                    bl var90;
                    if ((var90 = da.a_da.a_cd.b_bl(var46)) != null) {
                      if (var73 < 4) {
                        if (o.b_S == 1) {
                          cd.a(var90, var73, 1, cg.a_I(var1[var2 + 2]), 0, 0);
                        } else {
                          cd.a(var90, var73, 3, cg.a_I(var1[var2 + 2]), 0, 0);
                        }
                      } else if (o.b_S == 1) {
                        cd.a(var90, var73, 1, cg.a_I(var1[var2 + 2]), cg.a_I(var1[var2 + 3]), cg.a_I(var1[var2 + 4]));
                      } else {
                        cd.a(var90, var73, 3, cg.a_I(var1[var2 + 2]), cg.a_I(var1[var2 + 3]), cg.a_I(var1[var2 + 4]));
                      }
                    }

                    this.a(1);
                    return;
                  }

                  if (var1[var2].equals("人物走动")) {
                    int var45 = cg.a_I(var1[var2 + 1]);
                    int var72 = cg.a_I(var1[var2 + 2]);
                    bl var89;
                    if ((var89 = da.a_da.a_cd.b_bl(var45)) == null) {
                      this.a(2);
                      return;
                    }

                    if (var72 < 4) {
                      cd.a(var89, var72, 3, cg.a_I(var1[var2 + 3]), 0, 0);
                    } else {
                      cd.a(var89, var72, 3, cg.a_I(var1[var2 + 3]), cg.a_I(var1[var2 + 4]), cg.a_I(var1[var2 + 5]));
                    }

                    this.a(1);
                    return;
                  }

                  if (!var1[var2].equals("战斗胜利") && !var1[var2].equals("胜利动作")) {
                    if (!var1[var2].equals("战斗失败") && !var1[var2].equals("失败动作")) {
                      if (!var1[var2].equals("战斗逃跑") && !var1[var2].equals("逃跑动作")) {
                        if (var1[var2].equals("对话中选择")) {
                          if (da.a_da.a_cd.a_cf == null) {
                            da.a_da.a_cd.a_cf = new cf();
                            da.a_da.a_cd.a_cf.d();
                          }

                          da.a_da.a_cd.a_cf.b_AString = new String[var1.length - (var2 + 1)];

                          for (int var44 = var2 + 1; var44 < var1.length; var44++) {
                            da.a_da.a_cd.a_cf.b_AString[var44 - (var2 + 1)] = var1[var44];
                          }

                          da.a_da.a_cd.a_cf.c_B = cf.e_B;
                          da.a_da.a_cd.b_B = 2;
                          da.a_da.a_cd.a_cf.a_Z = true;
                          return;
                        }

                        if (var1[var2].equals("选择了")) {
                          if (da.a_da.a_cd.b_String != null && da.a_da.a_cd.b_String.equals(var1[var2 + 1])) {
                            if (this.b_B == 0) {
                              this.b_B = 1;
                            }

                            if (var2 + 2 < var1.length) {
                              this.a(var1, var2 + 2);
                              return;
                            }

                            this.a(0);
                            return;
                          }

                          this.b_B = 2;
                        } else if (var1[var2].equals("加入任务")) {
                          o.a(cg.a_S(var1[var2 + 1]));
                        } else if (var1[var2].equals("移除任务")) {
                          o.a(cg.a_S(var1[var2 + 1]), true);
                        } else {
                          if (var1[var2].equals("已发生事件")) {
                            int var43 = cg.a_I(var1[var2 + 1]);
                            if (!cg.a_Z(o.a_AI, var43)) {
                              this.b_B = 2;
                              this.a(0);
                              return;
                            }

                            if (this.b_B == 0) {
                              this.b_B = 1;
                            }

                            if (var2 + 2 < var1.length) {
                              this.a(var1, var2 + 2);
                            } else {
                              this.a(0);
                            }

                            return;
                          }

                          if (var1[var2].equals("未发生事件")) {
                            int var42 = cg.a_I(var1[var2 + 1]);
                            if (cg.a_Z(o.a_AI, var42)) {
                              this.b_B = 2;
                              this.a(0);
                              return;
                            }

                            if (this.b_B == 0) {
                              this.b_B = 1;
                            }

                            if (var2 + 2 < var1.length) {
                              this.a(var1, var2 + 2);
                            } else {
                              this.a(0);
                            }

                            return;
                          }

                          if (var1[var2].equals("有标志")) {
                            if (df.d_Z) {
                              if (this.b_B == 0) {
                                this.b_B = 1;
                              }

                              if (var2 + 1 < var1.length) {
                                this.a(var1, var2 + 1);
                                return;
                              }

                              this.a(0);
                              return;
                            }

                            this.b_B = 2;
                          } else if (var1[var2].equals("无标志")) {
                            if (!df.d_Z) {
                              if (this.b_B == 0) {
                                this.b_B = 1;
                              }

                              if (var2 + 1 < var1.length) {
                                this.a(var1, var2 + 1);
                                return;
                              }

                              this.a(0);
                              return;
                            }

                            this.b_B = 2;
                          } else if (var1[var2].equals("人物洗点")) {
                            int var3 = cg.a_I(var1[var2 + 1]) - 1;
                            o.a_Abl[var3].a_AI[9] = 0;
                            o.d(o.a_Abl[var3]);
                          } else {
                            if (var1[var2].equals("宠物数量")) {
                              String var41 = var1[var2 + 1];
                              int var71 = 0;

                              for (int var88 = 0; o.a_Acv != null && var88 < o.a_Acv.length; var88++) {
                                if (o.a_Acv[var88].a_B == o.e_S) {
                                  var71++;
                                }
                              }

                              if (var41.equals("大于等于")) {
                                if (var71 >= ct.c_B) {
                                  if (this.b_B == 0) {
                                    this.b_B = 1;
                                  }

                                  if (var2 + 3 < var1.length) {
                                    this.a(var1, var2 + 3);
                                  } else {
                                    this.a(0);
                                  }

                                  return;
                                } else {
                                  this.b_B = 2;
                                  this.a(0);
                                  return;
                                }
                              } else {
                                if (!var41.equals("小于")) {
                                  return;
                                }

                                if (var71 >= ct.c_B) {
                                  this.b_B = 2;
                                  this.a(0);
                                  return;
                                }

                                if (this.b_B == 0) {
                                  this.b_B = 1;
                                }

                                if (var2 + 3 < var1.length) {
                                  this.a(var1, var2 + 3);
                                } else {
                                  this.a(0);
                                }

                                return;
                              }
                            }

                            if (var1[var2].equals("随机结果")) {
                              String var40 = var1[var2 + 1];
                              int var70 = cg.a_I(var1[var2 + 2]);
                              if (var40.equals("大于")) {
                                if (a_I > var70) {
                                  if (this.b_B == 0) {
                                    this.b_B = 1;
                                  }

                                  if (var2 + 3 < var1.length) {
                                    this.a(var1, var2 + 3);
                                  } else {
                                    this.a(0);
                                  }

                                  return;
                                } else {
                                  this.b_B = 2;
                                  this.a(0);
                                  return;
                                }
                              } else if (var40.equals("小于")) {
                                if (a_I < var70) {
                                  if (this.b_B == 0) {
                                    this.b_B = 1;
                                  }

                                  if (var2 + 3 < var1.length) {
                                    this.a(var1, var2 + 3);
                                  } else {
                                    this.a(0);
                                  }

                                  return;
                                } else {
                                  this.b_B = 2;
                                  this.a(0);
                                  return;
                                }
                              } else {
                                if (!var40.equals("等于")) {
                                  return;
                                }

                                if (a_I != var70) {
                                  this.b_B = 2;
                                  this.a(0);
                                  return;
                                }

                                if (this.b_B == 0) {
                                  this.b_B = 1;
                                }

                                if (var2 + 3 < var1.length) {
                                  this.a(var1, var2 + 3);
                                } else {
                                  this.a(0);
                                }

                                return;
                              }
                            }

                            if (var1[var2].equals("随机数")) {
                              a_I = k.a(cg.a_I(var1[var2 + 1]), cg.a_I(var1[var2 + 2]));
                              this.a(5);
                              return;
                            }

                            if (var1[var2].equals("有道具")) {
                              if (o.b_I(cg.a_I(var1[var2 + 1])) > 0) {
                                if (this.b_B == 0) {
                                  this.b_B = 1;
                                }

                                if (var2 + 2 < var1.length) {
                                  this.a(var1, var2 + 2);
                                  return;
                                }

                                this.a(0);
                                return;
                              }

                              this.b_B = 2;
                            } else if (var1[var2].equals("无道具")) {
                              if (o.b_I(cg.a_I(var1[var2 + 1])) <= 0) {
                                if (this.b_B == 0) {
                                  this.b_B = 1;
                                }

                                if (var2 + 2 < var1.length) {
                                  this.a(var1, var2 + 2);
                                  return;
                                }

                                this.a(0);
                                return;
                              }

                              this.b_B = 2;
                            } else if (var1[var2].equals("无技能")) {
                              if (!cd.a_Z(cg.a_I(var1[var2 + 1]))) {
                                if (this.b_B == 0) {
                                  this.b_B = 1;
                                }

                                if (var2 + 2 < var1.length) {
                                  this.a(var1, var2 + 2);
                                  return;
                                }

                                this.a(0);
                                return;
                              }

                              this.b_B = 2;
                            } else if (var1[var2].equals("扣除技能")) {
                              o.a_V(o.a_Abl[0], cg.a_S(var1[var2 + 2]));
                            } else if (var1[var2].equals("扣除道具")) {
                              o.c(cg.a_I(var1[var2 + 1]), cg.a_I(var1[var2 + 2]));
                            } else if (var1[var2].equals("扣除装备")) {
                              o.c_V(cg.a_I(var1[var2 + 1]));
                            } else {
                              if (var1[var2].equals("结束任务") || var1[var2].equals("结束脚本")) {
                                this.d();
                                return;
                              }

                              if (var1[var2].equals("游戏存档")) {
                                o.a_Z((byte)0);
                                o.b((byte)0);
                                this.a(5);
                                return;
                              }

                              if (var1[var2].equals("游戏结局")) {
                                if (Main.music != null) {
                                  Main.music.b();
                                }

                                da.a_da.a_cd.e_B = cg.a_B(var1[var2 + 1]);
                                da.a_da.a_cd.a_B = 3;
                                da.a_da.a_cd.f();
                                return;
                              }

                              if (var1[var2].equals("游戏结局1")) {
                                if (Main.music != null) {
                                  Main.music.b();
                                }

                                da.a_da.a_cd.e_B = cg.a_B(var1[var2 + 1]);
                                o.b_Z = true;
                                da.a_da.a_cd.f();
                              } else {
                                if (var1[var2].equals("屏幕闪烁")) {
                                  da.a_da.a_cd.i_Z = true;
                                  da.a_da.a_cd.g_B = 5;
                                  this.a(1);
                                  return;
                                }

                                if (var1[var2].equals("场景抖动")) {
                                  da.a_da.a_cd.d_Z = true;
                                } else if (var1[var2].equals("场景抖动停")) {
                                  da.a_da.a_cd.d_Z = false;
                                } else {
                                  if (var1[var2].equals("加入队伍")) {
                                    int var39 = cg.a_I(var1[var2 + 1]);
                                    boolean var69 = false;

                                    for (int var86 = 0; var86 < da.a_da.a_cd.a_bj.a_Acz.length; var86++) {
                                      if (da.a_da.a_cd.a_bj.a_Acz[var86] != null
                                        && da.a_da.a_cd.a_bj.a_Acz[var86].h_S == var39
                                        && da.a_da.a_cd.a_bj.a_Acz[var86].i_B == 1) {
                                        var69 = true;
                                        bl var98;
                                        o.a(var98 = (bl)da.a_da.a_cd.a_bj.a_Acz[var86]);
                                        o.c(var98);
                                        var98.f_V();
                                        if (var98.h_S == 2 || var98.h_S == 3 || var98.h_S == 4 || var98.h_S == 5) {
                                          o.d(var98);
                                          var98.a_AI[3] = var98.a_AI[10];
                                          var98.a_AI[5] = var98.a_AI[11];
                                        }
                                        break;
                                      }
                                    }

                                    if (!var69) {
                                      bl var87;
                                      o.c(var87 = o.a_bl(var39));
                                      da.a_da.a_cd.a_bj.a(var87);
                                      o.d(var87);
                                      var87.a_AI[3] = var87.a_AI[10];
                                      var87.a_AI[5] = var87.a_AI[11];
                                    }

                                    this.a(0);
                                    return;
                                  }

                                  if (var1[var2].equals("离开队伍")) {
                                    o.f_V(cg.a_I(var1[var2 + 1]));
                                    da.a_da.a_cd.g();
                                  } else {
                                    if (var1[var2].equals("属性加点")) {
                                      ae.a();
                                      as.o_B = 2;
                                      da.a_da.a_cd.a_as.i();
                                      da.a_da.a_cd.a_as.b_Z = true;
                                      return;
                                    }

                                    if (var1[var2].equals("进入图鉴")) {
                                      int var38 = cg.a_I(var1[var2 + 1]);
                                      o.p_AS = cg.a_AS(o.p_AS, var38);
                                      da.a_da.a_cd.a_as = null;
                                      ae.a();
                                      da.a_da.a_cd.a_as.a_Z = true;
                                      da.a_da.a_cd.i();
                                      da.a_da.a_cd.a_as.i();
                                      da.a_da.a_cd.a_as.a(var38);
                                      da.a_da.a_cd.a_as.c(2);
                                      da.a_da.a_cd.a_as.i_I = var38;
                                      da.a_da.a_cd.a_as.a_de.a(true);
                                      return;
                                    }

                                    if (var1[var2].equals("元素加点")) {
                                      ae.a();
                                      da.a_da.a_cd.a_as.b_Z = true;
                                      as.o_B = 2;
                                      return;
                                    }

                                    if (var1[var2].equals("技能学习")) {
                                      da.a_da.a_cd.a("技能学习", cg.a_I(var1[var2 + 1]), cg.a_I(var1[var2 + 2]));
                                      return;
                                    }

                                    if (var1[var2].equals("寄宠物")) {
                                      da.a_da.a_cd.h();
                                      return;
                                    }

                                    if (var1[var2].equals("取宠物")) {
                                      da.a_da.a_cd.h();
                                      return;
                                    }

                                    if (var1[var2].equals("买道具")) {
                                      da.a_da.a_cd.a("买道具", cg.a_I(var1[var2 + 1]), 1);
                                      return;
                                    }

                                    if (var1[var2].equals("买装备")) {
                                      da.a_da.a_cd.a("买装备", cg.a_I(var1[var2 + 1]), 1);
                                      return;
                                    }

                                    if (var1[var2].equals("装备合成")) {
                                      da.a_da.a_cd.a("装备合成", cg.a_I(var1[var2 + 1]), 1);
                                      return;
                                    }

                                    if (var1[var2].equals("镶嵌")) {
                                      da.a_da.a_cd.a("镶嵌", 11, 1);
                                      return;
                                    }

                                    if (var1[var2].equals("改变属性")) {
                                      int var37 = cg.a_I(var1[var2 + 1]);
                                      int var68 = cg.a_I(var1[var2 + 2]);
                                      int var85 = cg.a_I(var1[var2 + 3]);

                                      for (int var97 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var97 < da.a_da.a_cd.a_bj.a_Acz.length; var97++) {
                                        if (da.a_da.a_cd.a_bj.a_Acz[var97] != null
                                          && da.a_da.a_cd.a_bj.a_Acz[var97].i_B == 1
                                          && ((bl)da.a_da.a_cd.a_bj.a_Acz[var97]).h_S == var37) {
                                          if (var68 < ((bl)da.a_da.a_cd.a_bj.a_Acz[var97]).a_AI.length) {
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var97]).a_AI[var68] = ((bl)da.a_da.a_cd.a_bj.a_Acz[var97]).a_AI[var68] + var85;
                                            o.a(new bl[]{(bl)da.a_da.a_cd.a_bj.a_Acz[var97]});
                                            o.d((bl)da.a_da.a_cd.a_bj.a_Acz[var97]);
                                            break;
                                          }

                                          System.out.println("改变属性越界,越界属性index" + var68);
                                        }
                                      }

                                      this.a(0);
                                      return;
                                    }

                                    if (var1[var2].equals("移除NPC")) {
                                      short var9 = cg.a_S(var1[var2 + 1]);

                                      for (int var4 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var4 < da.a_da.a_cd.a_bj.a_Acz.length; var4++) {
                                        if (da.a_da.a_cd.a_bj.a_Acz[var4] != null
                                          && da.a_da.a_cd.a_bj.a_Acz[var4].i_B == 1
                                          && ((bl)da.a_da.a_cd.a_bj.a_Acz[var4]).h_S == var9) {
                                          da.a_da.a_cd.a_bj.c(da.a_da.a_cd.a_bj.a_Acz[var4]);
                                        }
                                      }

                                      da.a_da.a_cd.a_bj.d();
                                      da.a_da.a_cd.a_bj.b();
                                    } else if (var1[var2].equals("改复活点")) {
                                      o.d_S = cg.a_S(var1[var2 + 1]);
                                      o.e_B = cg.a_B(var1[var2 + 2]);
                                    } else {
                                      if (var1[var2].equals("道具数量")) {
                                        short var36 = cg.a_S(var1[var2 + 1]);
                                        String var67 = var1[var2 + 2];
                                        int var84 = cg.a_I(var1[var2 + 3]);
                                        if (var67.equals("大于")) {
                                          if (o.b_I(var36) <= var84) {
                                            this.b_B = 2;
                                            break label1768;
                                          }

                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 4 < var1.length) {
                                            this.a(var1, var2 + 4);
                                          } else {
                                            this.a(0);
                                          }

                                          return;
                                        } else {
                                          if (!var67.equals("小于")) {
                                            break label1768;
                                          }

                                          if (o.b_I(var36) >= var84) {
                                            this.b_B = 2;
                                            break label1768;
                                          }

                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 4 < var1.length) {
                                            this.a(var1, var2 + 4);
                                          } else {
                                            this.a(0);
                                          }

                                          return;
                                        }
                                      }

                                      if (var1[var2].equals("判断属性")) {
                                        int var35 = cg.a_I(var1[var2 + 1]);
                                        int var66 = cg.a_I(var1[var2 + 2]);
                                        String var83 = var1[var2 + 3];
                                        int var96 = cg.a_I(var1[var2 + 4]);
                                        if (var83.equals("大于")) {
                                          boolean var99 = false;

                                          for (int var102 = 0; o.a_Aao != null && var102 < o.a_Aao.length; var102++) {
                                            if (o.a_Aao[var102].a_B == var35) {
                                              if (o.a_Aao[var102].a_AI[var66] > var96) {
                                                if (this.b_B == 0) {
                                                  this.b_B = 1;
                                                }

                                                if (var2 + 5 < var1.length) {
                                                  this.a(var1, var2 + 5);
                                                } else {
                                                  this.a(0);
                                                }

                                                var99 = true;
                                                break;
                                              }

                                              this.b_B = 2;
                                            }
                                          }

                                          if (!var99) {
                                            this.a(0);
                                            return;
                                          }
                                        } else {
                                          if (!var83.equals("小于")) {
                                            this.a(0);
                                            return;
                                          }

                                          boolean var100 = false;

                                          for (int var103 = 0; o.a_Aao != null && var103 < o.a_Aao.length; var103++) {
                                            if (o.a_Aao[var103].a_B == var35) {
                                              if (o.a_Aao[var103].a_AI[var66] < var96) {
                                                if (this.b_B == 0) {
                                                  this.b_B = 1;
                                                }

                                                if (var2 + 5 < var1.length) {
                                                  this.a(var1, var2 + 5);
                                                } else {
                                                  this.a(0);
                                                }

                                                var100 = true;
                                                break;
                                              }

                                              this.b_B = 2;
                                            }
                                          }

                                          if (!var100) {
                                            this.a(0);
                                            return;
                                          }
                                        }

                                        return;
                                      }

                                      if (var1[var2].equals("播放动作")) {
                                        int var34 = cg.a_I(var1[var2 + 1]);
                                        int var65 = cg.a_I(var1[var2 + 2]);
                                        int var82;
                                        if (var2 + 3 < var1.length) {
                                          var82 = cg.a_I(var1[var2 + 3]);
                                        } else {
                                          var82 = 1;
                                        }

                                        for (int var95 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var95 < da.a_da.a_cd.a_bj.a_Acz.length; var95++) {
                                          if (da.a_da.a_cd.a_bj.a_Acz[var95] != null
                                            && da.a_da.a_cd.a_bj.a_Acz[var95].i_B == 1
                                            && ((bl)da.a_da.a_cd.a_bj.a_Acz[var95]).h_S == var34) {
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var95]).a_bx.b(0);
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var95]).a_bx.a_V(var65);
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var95]).c_I = var65;
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var95]).b_I = var82;
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var95]).d_I = cg.a_I(var1[var2 + 4]);
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var95]).i_Z = true;
                                            return;
                                          }
                                        }

                                        return;
                                      }

                                      if (var1[var2].equals("飞行")) {
                                        if (o.a_Abl[0].h_S == 1) {
                                          o.h_String = "/av/long1.av";
                                        } else {
                                          o.h_String = "/av/long2.av";
                                        }

                                        o.a_Abl[0].a_bx = null;
                                        o.a_Abl[0].a_bx = new bx();
                                        o.a_Abl[0].a_bx.a(o.h_String, 1);

                                        for (int var33 = 0; var33 < o.a_Abl.length; var33++) {
                                          if (var33 != 0) {
                                            o.a_Abl[var33].j_Z = false;
                                          }
                                        }

                                        this.a(5);
                                        return;
                                      }

                                      if (var1[var2].equals("降落")) {
                                        o.a_Abl[0].a_bx.a(o.e_AString[0], 1);

                                        for (int var32 = 0; var32 < o.a_Abl.length; var32++) {
                                          if (var32 != 0) {
                                            o.a_Abl[var32].a(o.a_Abl[0].d_S, o.a_Abl[0].e_S);
                                            o.a_Abl[var32].j_Z = true;
                                          }
                                        }

                                        this.a(5);
                                        return;
                                      }

                                      if (var1[var2].equals("高级休息")) {
                                        for (int var30 = 0; var30 < o.a_Abl.length; var30++) {
                                          o.a_Abl[var30].a_AI[3] = o.a_Abl[var30].a_AI[10];
                                          o.a_Abl[var30].a_AI[5] = o.a_Abl[var30].a_AI[11];
                                        }

                                        o.a(o.a_Abl);

                                        for (int var31 = 0; o.a_Acv != null && var31 < o.a_Acv.length; var31++) {
                                          o.a_Acv[var31].a_AI[1] = o.a_Acv[var31].a_AI[2];
                                          o.a_Acv[var31].a_AI[4] = o.a_Acv[var31].a_AI[5];
                                        }

                                        o.a(o.a_Acv);
                                        da.a_da.a_cd.f_B = 30;
                                        da.a_da.a_cd.e_Z = true;
                                        return;
                                      }

                                      if (var1[var2].equals("休息")) {
                                        for (int var28 = 0; var28 < o.a_Abl.length; var28++) {
                                          o.a_Abl[var28].a_AI[3] = o.a_Abl[var28].a_AI[10];
                                        }

                                        o.a(o.a_Abl);

                                        for (int var29 = 0; o.a_Acv != null && var29 < o.a_Acv.length; var29++) {
                                          o.a_Acv[var29].a_AI[1] = o.a_Acv[var29].a_AI[2];
                                        }

                                        o.a(o.a_Acv);
                                        da.a_da.a_cd.f_B = 30;
                                        da.a_da.a_cd.e_Z = true;
                                        return;
                                      }

                                      if (var1[var2].equals("载入人物")) {
                                        int var27 = cg.a_I(var1[var2 + 1]);
                                        int var64 = cg.a_I(var1[var2 + 2]);
                                        int var81 = cg.a_I(var1[var2 + 3]);
                                        int var94 = cg.a_I(var1[var2 + 4]);
                                        boolean var7 = false;

                                        for (int var8 = 0; var8 < da.a_da.a_cd.a_bj.a_Acz.length; var8++) {
                                          if (da.a_da.a_cd.a_bj.a_Acz[var8] != null
                                            && da.a_da.a_cd.a_bj.a_Acz[var8].h_S == var27
                                            && da.a_da.a_cd.a_bj.a_Acz[var8].i_B == 1) {
                                            var7 = true;
                                            o.a((bl)da.a_da.a_cd.a_bj.a_Acz[var8]);
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var8]).d_S = (short)var64;
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var8]).e_S = (short)var81;
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var8]).a_V(var94);
                                            ((bl)da.a_da.a_cd.a_bj.a_Acz[var8]).f_V();
                                            break;
                                          }
                                        }

                                        if (!var7) {
                                          bl var101;
                                          o.a(var101 = o.a_bl(var27));
                                          var101.d_S = (short)var64;
                                          var101.e_S = (short)var81;
                                          var101.a_V(var94);
                                          var101.f_V();
                                          da.a_da.a_cd.a_bj.a(var101);
                                        }

                                        this.a(0);
                                        return;
                                      }

                                      if (var1[var2].equals("有装备")) {
                                        cg.a_I(var1[var2 + 1]);
                                        if (o.c_Z(cg.a_I(var1[var2 + 2]))) {
                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 3 < var1.length) {
                                            this.a(var1, var2 + 3);
                                            return;
                                          }

                                          this.a(0);
                                          return;
                                        }

                                        this.b_B = 2;
                                      } else if (var1[var2].equals("无装备")) {
                                        cg.a_I(var1[var2 + 1]);
                                        if (!o.c_Z(cg.a_I(var1[var2 + 2]))) {
                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 3 < var1.length) {
                                            this.a(var1, var2 + 3);
                                            return;
                                          }

                                          this.a(0);
                                          return;
                                        }

                                        this.b_B = 2;
                                      } else if (var1[var2].equals("有技能")) {
                                        if (o.a_Z(o.a_Abl[0], cg.a_I(var1[var2 + 1]))) {
                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 3 < var1.length) {
                                            this.a(var1, var2 + 3);
                                            return;
                                          }

                                          this.a(0);
                                          return;
                                        }

                                        this.b_B = 2;
                                      } else if (var1[var2].equals("技能等级")) {
                                        if (o.a_Z(o.a_Abl[0], cg.a_I(var1[var2 + 1]))) {
                                          int var10 = -1;

                                          for (int var49 = 0; var49 < o.a_Abl[0].a_Ad.length; var49++) {
                                            if (o.a_Abl[0].a_Ad[var49].a_S == cg.a_I(var1[var2 + 1])) {
                                              var10 = var49;
                                              break;
                                            }
                                          }

                                          if (var1[var2 + 2].equals("小于")) {
                                            if (o.a_Abl[0].a_Ad[var10].a_B < cg.a_I(var1[var2 + 3])) {
                                              if (this.b_B == 0) {
                                                this.b_B = 1;
                                              }

                                              if (var2 + 4 < var1.length) {
                                                this.a(var1, var2 + 4);
                                              } else {
                                                this.a(0);
                                              }

                                              return;
                                            } else {
                                              this.b_B = 2;
                                              this.a(0);
                                              return;
                                            }
                                          } else {
                                            if (!var1[var2 + 2].equals("大于")) {
                                              return;
                                            }

                                            if (o.a_Abl[0].a_Ad[var10].a_B <= cg.a_I(var1[var2 + 3])) {
                                              this.b_B = 2;
                                              this.a(0);
                                              return;
                                            }

                                            if (this.b_B == 0) {
                                              this.b_B = 1;
                                            }

                                            if (var2 + 4 < var1.length) {
                                              this.a(var1, var2 + 4);
                                            } else {
                                              this.a(0);
                                            }

                                            return;
                                          }
                                        }

                                        this.b_B = 2;
                                      } else if (var1[var2].equals("有宠物")) {
                                        if (o.d_Z(cg.a_I(var1[var2 + 1]))) {
                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 2 < var1.length) {
                                            this.a(var1, var2 + 2);
                                            return;
                                          }

                                          this.a(0);
                                          return;
                                        }

                                        this.b_B = 2;
                                      } else {
                                        if (var1[var2].equals("有战宠")) {
                                          if (k.a_I() <= 0) {
                                            this.b_B = 2;
                                            this.a(0);
                                            return;
                                          }

                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 1 < var1.length) {
                                            this.a(var1, var2 + 1);
                                          } else {
                                            this.a(0);
                                          }

                                          return;
                                        }

                                        if (var1[var2].equals("无战宠")) {
                                          if (k.a_I() != 0) {
                                            this.b_B = 2;
                                            this.a(0);
                                            return;
                                          }

                                          if (this.b_B == 0) {
                                            this.b_B = 1;
                                          }

                                          if (var2 + 1 < var1.length) {
                                            this.a(var1, var2 + 1);
                                          } else {
                                            this.a(0);
                                          }

                                          return;
                                        }

                                        if (var1[var2].equals("无任务")) {
                                          if (!cg.a_Z(o.n_AS, cg.a_I(var1[var2 + 1]))) {
                                            if (this.b_B == 0) {
                                              this.b_B = 1;
                                            }

                                            if (var2 + 2 < var1.length) {
                                              this.a(var1, var2 + 2);
                                              return;
                                            }

                                            this.a(0);
                                            return;
                                          }

                                          this.b_B = 2;
                                        } else {
                                          if (var1[var2].equals("有任务")) {
                                            short var26 = cg.a_S(var1[var2 + 1]);
                                            boolean var63 = false;

                                            for (int var80 = 0; o.a_Abp != null && var80 < o.a_Abp.length; var80++) {
                                              if (o.a_Abp[var80].a_S == var26 && !o.a_Abp[var80].a_Z) {
                                                var63 = true;
                                                break;
                                              }
                                            }

                                            if (!cg.a_Z(o.n_AS, cg.a_I(var1[var2 + 1])) || !var63) {
                                              this.b_B = 2;
                                              this.a(0);
                                              return;
                                            }

                                            if (this.b_B == 0) {
                                              this.b_B = 1;
                                            }

                                            if (var2 + 2 < var1.length) {
                                              this.a(var1, var2 + 2);
                                            } else {
                                              this.a(0);
                                            }

                                            return;
                                          }

                                          if (var1[var2].equals("任务完成")) {
                                            short var25 = cg.a_S(var1[var2 + 1]);
                                            boolean var62 = false;

                                            for (int var79 = 0; o.a_Abp != null && var79 < o.a_Abp.length; var79++) {
                                              if (o.a_Abp[var79].a_S == var25 && o.a_Abp[var79].a_Z) {
                                                var62 = true;
                                                break;
                                              }
                                            }

                                            if (!var62) {
                                              this.b_B = 2;
                                              this.a(0);
                                              return;
                                            }

                                            if (this.b_B == 0) {
                                              this.b_B = 1;
                                            }

                                            if (var2 + 2 < var1.length) {
                                              this.a(var1, var2 + 2);
                                            } else {
                                              this.a(0);
                                            }

                                            return;
                                          }

                                          if (var1[var2].equals("是否装备")) {
                                            if (o.a_Abl[0].a_Ack[2] != null && o.a_Abl[0].a_Ack[0] != null) {
                                              if (this.b_B == 0) {
                                                this.b_B = 1;
                                              }

                                              if (var2 + 1 < var1.length) {
                                                this.a(var1, var2 + 1);
                                                return;
                                              }

                                              this.a(0);
                                              return;
                                            }

                                            this.b_B = 2;
                                          } else {
                                            if (var1[var2].equals("金钱数量")) {
                                              String var24 = var1[var2 + 1];
                                              int var61 = cg.a_I(var1[var2 + 2]);
                                              if (var24.equals("大于")) {
                                                if (o.a_I <= var61) {
                                                  this.b_B = 2;
                                                  break label1887;
                                                }

                                                if (this.b_B == 0) {
                                                  this.b_B = 1;
                                                }

                                                if (var2 + 3 < var1.length) {
                                                  this.a(var1, var2 + 3);
                                                } else {
                                                  this.a(0);
                                                }

                                                return;
                                              } else {
                                                if (!var24.equals("小于")) {
                                                  break label1887;
                                                }

                                                if (o.a_I >= var61) {
                                                  this.b_B = 2;
                                                  break label1887;
                                                }

                                                if (this.b_B == 0) {
                                                  this.b_B = 1;
                                                }

                                                if (var2 + 3 < var1.length) {
                                                  this.a(var1, var2 + 3);
                                                } else {
                                                  this.a(0);
                                                }

                                                return;
                                              }
                                            }

                                            if (var1[var2].equals("平移")) {
                                              short var23 = cg.a_S(var1[var2 + 1]);
                                              byte var60 = cg.a_B(var1[var2 + 2]);
                                              bl var78 = null;

                                              for (int var93 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var93 < da.a_da.a_cd.a_bj.a_Acz.length; var93++) {
                                                if (da.a_da.a_cd.a_bj.a_Acz[var93] != null
                                                  && da.a_da.a_cd.a_bj.a_Acz[var93].i_B == 1
                                                  && ((bl)da.a_da.a_cd.a_bj.a_Acz[var93]).h_S == var23) {
                                                  var78 = (bl)da.a_da.a_cd.a_bj.a_Acz[var93];
                                                  break;
                                                }
                                              }

                                              if (var78 != null) {
                                                switch (var60) {
                                                  case 0:
                                                    var78.e_S = (short)(var78.e_S + cg.a_I(var1[var2 + 3]));
                                                    break;
                                                  case 1:
                                                    var78.e_S = (short)(var78.e_S - cg.a_I(var1[var2 + 3]));
                                                    break;
                                                  case 2:
                                                    var78.d_S = (short)(var78.d_S - cg.a_I(var1[var2 + 3]));
                                                    break;
                                                  case 3:
                                                    var78.d_S = (short)(var78.d_S + cg.a_I(var1[var2 + 3]));
                                                }
                                              }

                                              this.a(0);
                                              return;
                                            }

                                            if (var1[var2].equals("连续执行")) {
                                              this.a(0);
                                              this.a_S = cg.a_S(var1[var2 + 1]);
                                              this.b_S = this.a_S;
                                              return;
                                            }

                                            if (var1[var2].equals("变速走动")) {
                                              int var22 = cg.a_I(var1[var2 + 1]);
                                              int var59 = cg.a_I(var1[var2 + 2]);
                                              int var77 = cg.a_I(var1[var2 + 3]);
                                              bl var6;
                                              if ((var6 = da.a_da.a_cd.b_bl(var22)) != null) {
                                                if (var77 < 4) {
                                                  cd.a(var6, var77, var59, cg.a_I(var1[var2 + 4]), 0, 0);
                                                } else {
                                                  cd.a(var6, var77, var59, cg.a_I(var1[var2 + 4]), cg.a_I(var1[var2 + 5]), cg.a_I(var1[var2 + 6]));
                                                }
                                              }

                                              this.a(1);
                                              return;
                                            }

                                            if (var1[var2].equals("改变金钱")) {
                                              int var21 = cg.a_I(var1[var2 + 1]);
                                              if ((o.a_I += var21) < 0) {
                                                o.a_I = 0;
                                              }

                                              this.a(0);
                                              return;
                                            }

                                            if (var1[var2].equals("继承条件")) {
                                              if (this.f_Z) {
                                                this.a(var1, var2 + 1);
                                                return;
                                              }

                                              this.a(0);
                                              return;
                                            }

                                            if (var1[var2].equals("黑屏")) {
                                              da.a_da.a_cd.u_Z = true;
                                              this.a(1);
                                              return;
                                            }

                                            if (var1[var2].equals("恢复黑屏")) {
                                              da.a_da.a_cd.v_Z = true;
                                              this.a(1);
                                              return;
                                            }

                                            if (var1[var2].equals("上下拉屏")) {
                                              da.a_da.a_cd.j_Z = true;
                                              da.a_da.a_cd.h_S = cg.a_S(var1[var2 + 1]);
                                              this.a(1);
                                              return;
                                            }

                                            if (var1[var2].equals("恢复拉屏")) {
                                              da.a_da.a_cd.k_Z = true;
                                              this.a(1);
                                              return;
                                            }

                                            if (var1[var2].equals("改变主角")) {
                                              if (o.e_S != cg.a_S(var1[var2 + 1])) {
                                                k.a(o.e_S = cg.a_S(var1[var2 + 1]));
                                              }

                                              this.a(0);
                                              return;
                                            }

                                            if (var1[var2].equals("控制抖动")) {
                                              int var20 = cg.a_I(var1[var2 + 1]);

                                              for (int var58 = 0; var58 < da.a_da.a_cd.a_bj.a_Acz.length; var58++) {
                                                if (da.a_da.a_cd.a_bj.a_Acz[var58] != null
                                                  && da.a_da.a_cd.a_bj.a_Acz[var58].h_S == var20
                                                  && da.a_da.a_cd.a_bj.a_Acz[var58].i_B == 1) {
                                                  ((bl)da.a_da.a_cd.a_bj.a_Acz[var58]).g_Z = !((bl)da.a_da.a_cd.a_bj.a_Acz[var58]).g_Z;
                                                }
                                              }

                                              this.a(0);
                                              return;
                                            }

                                            if (var1[var2].equals("载入宠物")) {
                                              o.a_V(cg.a_I(var1[var2 + 1]), -1);
                                            } else {
                                              if (var1[var2].equals("移除宠物")) {
                                                int var19 = cg.a_I(var1[var2 + 1]);

                                                for (int var57 = 0; o.a_Acv != null && var57 < o.a_Acv.length; var57++) {
                                                  if (o.a_Acv[var57].a_I == var19) {
                                                    o.e_V(o.a_Acv[var57].b_I);
                                                    break;
                                                  }
                                                }

                                                this.a(0);
                                                return;
                                              }

                                              if (var1[var2].equals("开始下雨")) {
                                                da.a_da.a_cd.n();
                                                this.a(1);
                                                return;
                                              }

                                              if (var1[var2].equals("结束下雨")) {
                                                da.a_da.a_cd.m_Z = false;
                                                da.a_da.a_cd.a_Abi = null;
                                              } else {
                                                if (var1[var2].equals("有同伴")) {
                                                  boolean var18 = false;
                                                  int var56 = cg.a_I(var1[var2 + 1]);

                                                  for (int var76 = 0; var76 < da.a_da.a_cd.a_bj.a_Acz.length; var76++) {
                                                    if (da.a_da.a_cd.a_bj.a_Acz[var76] != null
                                                      && da.a_da.a_cd.a_bj.a_Acz[var76].h_S == var56
                                                      && da.a_da.a_cd.a_bj.a_Acz[var76].i_B == 1) {
                                                      var18 = true;
                                                      break;
                                                    }
                                                  }

                                                  if (!var18) {
                                                    this.b_B = 2;
                                                    this.a(0);
                                                    return;
                                                  }

                                                  if (this.b_B == 0) {
                                                    this.b_B = 1;
                                                  }

                                                  if (var2 + 2 < var1.length) {
                                                    this.a(var1, var2 + 2);
                                                  } else {
                                                    this.a(0);
                                                  }

                                                  return;
                                                }

                                                if (var1[var2].equals("开启副本")) {
                                                  short var11 = cg.a_S(var1[var2 + 1]);
                                                  short var50 = cg.a_S(var1[var2 + 2]);
                                                  short var5 = cg.a_S(var1[var2 + 3]);
                                                  k.a(var11, var50, var5);
                                                } else if (var1[var2].equals("是否开启")) {
                                                  if (da.a_da.a_cd.a_bx != null) {
                                                    if (this.b_B == 0) {
                                                      this.b_B = 1;
                                                    }

                                                    if (var2 + 1 < var1.length) {
                                                      this.a(var1, var2 + 1);
                                                      return;
                                                    }

                                                    this.a(0);
                                                    return;
                                                  }

                                                  this.b_B = 2;
                                                } else {
                                                  if (var1[var2].equals("主角ID")) {
                                                    short var17 = cg.a_S(var1[var2 + 1]);
                                                    if (o.e_S != var17) {
                                                      this.b_B = 2;
                                                      this.a(0);
                                                      return;
                                                    }

                                                    if (this.b_B == 0) {
                                                      this.b_B = 1;
                                                    }

                                                    if (var2 + 2 < var1.length) {
                                                      this.a(var1, var2 + 2);
                                                    } else {
                                                      this.a(0);
                                                    }

                                                    return;
                                                  }

                                                  if (var1[var2].equals("有随机任务")) {
                                                    boolean var16 = false;

                                                    for (int var55 = 0; o.a_Abp != null && var55 < o.a_Abp.length; var55++) {
                                                      if (o.a_Abp[var55].a_B == 3 && !o.a_Abp[var55].a_Z) {
                                                        var16 = true;
                                                        break;
                                                      }
                                                    }

                                                    if (!var16) {
                                                      this.b_B = 2;
                                                      this.a(0);
                                                      return;
                                                    }

                                                    if (this.b_B == 0) {
                                                      this.b_B = 1;
                                                    }

                                                    if (var2 + 1 < var1.length) {
                                                      this.a(var1, var2 + 1);
                                                    } else {
                                                      this.a(0);
                                                    }

                                                    return;
                                                  }

                                                  if (var1[var2].equals("无随机任务")) {
                                                    boolean var15 = false;

                                                    for (int var54 = 0; o.a_Abp != null && var54 < o.a_Abp.length; var54++) {
                                                      if (o.a_Abp[var54].a_B == 3) {
                                                        var15 = true;
                                                        break;
                                                      }
                                                    }

                                                    if (var15) {
                                                      this.b_B = 2;
                                                      this.a(0);
                                                      return;
                                                    }

                                                    if (this.b_B == 0) {
                                                      this.b_B = 1;
                                                    }

                                                    if (var2 + 1 < var1.length) {
                                                      this.a(var1, var2 + 1);
                                                    } else {
                                                      this.a(0);
                                                    }

                                                    return;
                                                  }

                                                  if (var1[var2].equals("随机任务")) {
                                                    int var12 = cg.a_I(var1[var2 + 1]);
                                                    int var51 = k.b(0, this.a_AAS[var12 - 1].length - 1);
                                                    this.f_S = this.a_AAS[var12 - 1][var51];
                                                  } else {
                                                    if (var1[var2].equals("显示任务")) {
                                                      da.a_da.a_cd.c_String = null;
                                                      if (var1.length - var2 > 1) {
                                                        da.a_da.a_cd.g_S = cg.a_S(var1[var2 + 1]);
                                                      } else {
                                                        da.a_da.a_cd.g_S = this.f_S;
                                                      }

                                                      da.a_da.a_cd.b_B = 9;
                                                      da.a_da.a_cd.c_B = 1;
                                                      return;
                                                    }

                                                    if (var1[var2].equals("完成随机任务")) {
                                                      boolean var14 = false;

                                                      for (int var53 = 0; o.a_Abp != null && var53 < o.a_Abp.length; var53++) {
                                                        if (o.a_Abp[var53].a_S >= 37 && o.a_Abp[var53].a_S <= 50 && o.a_Abp[var53].a_Z) {
                                                          var14 = true;
                                                          break;
                                                        }
                                                      }

                                                      if (!var14) {
                                                        this.b_B = 2;
                                                        this.a(0);
                                                        return;
                                                      }

                                                      if (this.b_B == 0) {
                                                        this.b_B = 1;
                                                      }

                                                      if (var2 + 1 < var1.length) {
                                                        this.a(var1, var2 + 1);
                                                      } else {
                                                        this.a(0);
                                                      }

                                                      return;
                                                    }

                                                    if (var1[var2].equals("移除随机任务")) {
                                                      short var13 = 0;

                                                      for (int var52 = 0; o.a_Abp != null && var52 < o.a_Abp.length; var52++) {
                                                        if (o.a_Abp[var52].a_S >= 37 && o.a_Abp[var52].a_S <= 50 && o.a_Abp[var52].a_Z) {
                                                          var13 = o.a_Abp[var52].a_S;
                                                          break;
                                                        }
                                                      }

                                                      if (var13 > 0) {
                                                        o.a(var13, true);
                                                        this.a(0);
                                                      }

                                                      return;
                                                    }

                                                    if (var1[var2].equals("职业等级")) {
                                                      if (o.a_Abl[0].f_B == cg.a_B(var1[var2 + 1])) {
                                                        if (this.b_B == 0) {
                                                          this.b_B = 1;
                                                        }

                                                        if (var2 + 2 < var1.length) {
                                                          this.a(var1, var2 + 2);
                                                          return;
                                                        }

                                                        this.a(0);
                                                        return;
                                                      }

                                                      this.b_B = 2;
                                                    } else if (var1[var2].equals("改变职业等级")) {
                                                      o.a_Abl[0].f_B++;
                                                    }
                                                  }
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      } else {
                        if (this.a_B == 1) {
                          if (this.b_B == 0) {
                            this.b_B = 1;
                          }

                          if (var2 + 1 < var1.length) {
                            this.a(var1, var2 + 1);
                            return;
                          }

                          this.a(0);
                          return;
                        }

                        this.b_B = 2;
                      }
                    } else {
                      if (this.a_B == 0) {
                        if (this.b_B == 0) {
                          this.b_B = 1;
                        }

                        if (var2 + 1 < var1.length) {
                          this.a(var1, var2 + 1);
                          return;
                        }

                        this.a(0);
                        return;
                      }

                      this.b_B = 2;
                    }
                  } else {
                    if (this.a_B == 2) {
                      if (this.b_B == 0) {
                        this.b_B = 1;
                      }

                      if (var2 + 1 < var1.length) {
                        this.a(var1, var2 + 1);
                        return;
                      }

                      this.a(0);
                      return;
                    }

                    this.b_B = 2;
                  }
                }
              }
            }

            this.a(0);
            return;
          }

          this.a(0);
          return;
        }

        this.a(0);
      }
    }
  }

  public final void a(int var1) {
    if (var1 == 0) {
      this.c_S++;
      if (this.b_S > 0) {
        this.b_S--;
      }
    } else if (var1 == 1) {
      if (this.a_S > 0) {
        this.c_S++;
        if (at.e_Z) {
          System.out.println("脚本继续执行");
        }
      } else if (at.e_Z) {
        System.out.println("脚本暂停");
      }
    } else if (var1 >= 2) {
      if (this.b_S > 0) {
        this.b_S--;
      }

      if (this.b_S == 0) {
        this.c_S++;
      }
    }

    if (this.b_B == 1) {
      this.f_Z = true;
    } else if (this.b_B == 2) {
      this.f_Z = false;
    }

    this.b_B = 0;
  }
}
