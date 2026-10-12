import java.util.Vector;

public final class ae {
  static bl a_bl;

  public static void a(int var0) {
    if (da.a_da.a_cd.a_B == 2 && da.a_da.a_cd.h_Z) {
      if (da.a_da.a_cd.b_B == 1) {
        if (da.a_da.a_cd.a_ci.a_Z && !da.a_da.a_cd.l_Z) {
          return;
        }

        if (var0 == 8) {
          if (da.a_da.a_cd.l_Z) {
            if (da.a_da.a_cd.a_cl.a_S == 1) {
              da.a_da.a_cd.a_I++;
              return;
            }

            if (da.a_da.a_cd.a_I >= 4) {
              da.a_da.a_cd.a_I -= 2;
              return;
            }

            da.a_da.a_cd.a_I = 2;
            return;
          }

          if (da.a_da.a_cd.a_ci.b_Z) {
            da.a_da.a_cd.a_ci.a(da.a_da.a_cd.a_ci.b_bz);
            return;
          }

          if (da.a_da.a_cd.b_Z) {
            ax var10 = da.a_da.a_cd.a_ax;
            boolean var15 = false;
            if (var10.b_B == 0) {
              var15 = true;
            } else if (var10.b_B == 1) {
              for (int var17 = 0; o.a_AAS != null && var17 < o.a_AAS.length; var17++) {
                if (o.a_AAS[var17][0] == 19 && o.a_AAS[var17][1] > 0) {
                  o.c(19, 1);
                  var15 = true;
                  break;
                }
              }
            }

            Vector var18 = new Vector();
            if (var15) {
              byte var19 = 1;
              var10.a_B = (byte)var19;
              if (!cg.a_Z(o.d_AS, var10.a_S)) {
                o.d_AS = cg.a_AS(o.d_AS, var10.a_S);
              } else {
                var10.c_B = 1;
              }

              String[] var16 = null;
              String[] var20 = null;
              if (var10.c_B == 1) {
                var18.addElement("Rương đã mở");
              } else {
                if (var10.b_S > 0) {
                  var18.addElement("Nhận vàng " + var10.b_S);
                  o.a_I = o.a_I + var10.b_S;
                  var10.b_S = 0;
                }

                if (var10.a_AAS != null) {
                  if (o.a_AAString == null) {
                    o.a_AAString = cg.a_AAString(cg.a_String("/bin/item.bin"), "item", "itemend", null, "\t");
                  }

                  for (int var21 = 0; var21 < o.a_AAString.length; var21++) {
                    var16 = cg.a(var16, o.a_AAString[var21][1]);
                  }
                }

                if (var10.b_AAS != null) {
                  if (o.c_AAString == null) {
                    o.c_AAString = cg.a_AAString(cg.a_String("/bin/equip.bin"), "equip", "equipend", null, "\t");
                  }

                  for (int var22 = 0; var22 < o.c_AAString.length; var22++) {
                    var20 = cg.a(var20, o.c_AAString[var22][1]);
                  }
                }

                for (int var23 = 0; var10.a_AAS != null && var23 < var10.a_AAS.length; var23++) {
                  var18.addElement("Nhận " + var16[var10.a_AAS[var23][0] - 1]);
                  o.b(var10.a_AAS[var23][0], var10.a_AAS[var23][1]);
                }

                var10.a_AAS = null;

                for (int var24 = 0; var10.b_AAS != null && var24 < var10.b_AAS.length; var24++) {
                  var18.addElement("Nhận " + var20[var10.b_AAS[var24][0] - 1]);
                  o.d(var10.b_AAS[var24][0], -1);
                }

                var10.b_AAS = null;
              }

              if (var18.size() <= 0) {
                var18.addElement("Rương trống rỗng");
              }
            } else {
              var18.addElement("Không có Chìa vạn năng");
            }

            da.a_da.a_cd.a_AString = new String[var18.size()];
            var18.copyInto(da.a_da.a_cd.a_AString);
            da.a_da.a_cd.d_B = 0;
            da.a_da.a_cd.b_B = 3;
            return;
          }

          if (da.a_da.a_cd.c_Z) {
            if (o.a_Abl[0].b_Z) {
              da.a_da.a("Đang bay không thể nói chuyện", 10);
              return;
            }

            a_bl = da.a_da.a_cd.b_bl;
            boolean var8 = false;

            for (int var12 = 0; o.a_Abl != null && var12 < o.a_Abl.length; var12++) {
              if (a_bl == o.a_Abl[var12]) {
                var8 = true;
              }
            }

            if (var8) {
              return;
            }

            if (a_bl.h_S > 0) {
              String var13;
              if ((var13 = cg.a_String("/bin/s" + o.b_S + "c.bin")) == null) {
                return;
              }

              String var2;
              if ((var2 = cg.a(var13, "role" + a_bl.h_S + ":", "role" + a_bl.h_S + "End")) == null) {
                return;
              }

              byte var14 = cg.a_B(var2, "amount");
              String[][] var9 = null;

              for (int var5 = 0; var5 < var14; var5++) {
                String var4 = cg.a(var2, "group" + (var5 + 1) + ":", "group" + (var5 + 1) + "End");
                byte var3 = cg.a_B(var2, "phase");
                if (var4 != null) {
                  if (var3 == 0 || var3 == o.a_B) {
                    var9 = cg.a_AAString(var4, "script:", "scriptEnd", null);
                  }
                  break;
                }
              }

              if (var9 == null) {
                return;
              }

              bz var25;
              (var25 = new bz()).a_AAString = var9;
              var25.a_bl = a_bl;
              da.a_da.a_cd.a_ci.a(var25);
              a_bl = null;
            }

            return;
          }
        } else {
          if (var0 == b.a_S) {
            if (!da.a_da.a_cd.l_Z) {
              a();
              as.o_B = 1;
              da.a_da.a_cd.a_as.i();
              return;
            }

            if (da.a_da.a_cd.a_cl.a_S == 1) {
              da.a_da.a_cd.a_I++;
              return;
            }

            if (da.a_da.a_cd.a_I >= 4) {
              da.a_da.a_cd.a_I -= 2;
              return;
            }

            da.a_da.a_cd.a_I = 2;
            return;
          }

          if (var0 == b.b_S) {
            if (da.a_da.a_cd.l_Z) {
              da.a_da.a_cd.l_Z = false;
              da.a_da.a_cd.a_ci.a(5);
              da.a_da.a_cd.a_cl.a_AString = null;
              return;
            }

            df.a("/bin/smsmenu.bin");
            if (at.f_Z) {
              da.a_da.a_cd.e();
              return;
            }
          } else {
            if (var0 == 57) {
              b();
              return;
            }

            if (var0 == 35 && at.e_Z) {
              o.a_Abl[0].f_Z = !o.a_Abl[0].f_Z;
              return;
            }
          }
        }
      } else if (da.a_da.a_cd.b_B == 3) {
        if (da.a_da.a_cd.a_dc != null && da.a_da.a_cd.a_dc.a_AString != null) {
          if (var0 == 8) {
            if (da.a_da.a_cd.d_B + da.a_da.a_cd.d_S < da.a_da.a_cd.a_dc.a_AString.length) {
              da.a_da.a_cd.d_B++;
              return;
            }

            da.a_da.a_cd.b_B = 1;
            da.a_da.a_cd.a_AString = null;
            da.a_da.a_cd.d_B = 0;
            da.a_da.a_cd.a_ci.a(0);
            return;
          }

          if (var0 == 6) {
            if (da.a_da.a_cd.d_B + da.a_da.a_cd.d_S < da.a_da.a_cd.a_dc.a_AString.length) {
              da.a_da.a_cd.d_B++;
              return;
            }
          } else if (var0 == 1 && da.a_da.a_cd.d_B > 0) {
            da.a_da.a_cd.d_B--;
            return;
          }
        }
      } else if (da.a_da.a_cd.b_B == 10) {
        if (var0 == b.a_S || var0 == 8) {
          df.a("/bin/smsmenu.bin");
          da.a_da.a_cd.a_df.b(6);
          return;
        }
      } else if (da.a_da.a_cd.b_B == 2) {
        if (var0 == 8) {
          if (da.a_da.a_cd.a_cf.c_B == 0) {
            if (da.a_da.a_cd.a_cf != null) {
              if (da.a_da.a_cd.a_cf.a_Z()) {
                if (da.a_da.a_cd.a_cf.a_B >= da.a_da.a_cd.a_cf.a_AString.length - 1) {
                  if (da.a_da.a_cd.a_cf.a_bl != null) {
                    boolean var7 = false;
                    if (da.a_da.a_cd.a_cf.a_bl.a_B == 1 && da.a_da.a_cd.a_cf.a_bl.a_bx.d() != 8 && da.a_da.a_cd.a_cf.a_bl.a_bx.d() != 4) {
                      var7 = true;
                    }

                    if (!var7) {
                      da.a_da.a_cd.a_cf.a_bl.a_V(da.a_da.a_cd.a_cf.a_bl.h_B);
                      da.a_da.a_cd.a_cf.a_bl.c_V();
                    }

                    da.a_da.a_cd.a_cf.a_bl.g_V();
                  }

                  da.a_da.a_cd.k();
                  da.a_da.a_cd.b_B = 1;
                  return;
                }

                da.a_da.a_cd.a_cf.f();
                return;
              }

              da.a_da.a_cd.a_cf.c();
              return;
            }
          } else if (da.a_da.a_cd.a_cf.c_B == cf.e_B) {
            da.a_da.a_cd.b_String = da.a_da.a_cd.a_cf.b_AString[da.a_da.a_cd.a_cf.b_B];
            da.a_da.a_cd.a_cf.b_B = 0;
            da.a_da.a_cd.a_cf.a_S = 0;
            da.a_da.a_cd.k();
            da.a_da.a_cd.b_B = 1;
            return;
          }
        } else if (var0 == 6) {
          if (da.a_da.a_cd.a_cf.c_B == 0) {
            if (da.a_da.a_cd.a_cf != null) {
              da.a_da.a_cd.a_cf.c();
              return;
            }
          } else if (da.a_da.a_cd.a_cf.c_B == cf.e_B) {
            if (da.a_da.a_cd.a_cf.b_B < da.a_da.a_cd.a_cf.b_AString.length - 1) {
              da.a_da.a_cd.a_cf.b_B++;
            }

            if (da.a_da.a_cd.a_cf.b_B - da.a_da.a_cd.a_cf.a_S == cf.b_S) {
              da.a_da.a_cd.a_cf.a_S++;
              return;
            }
          }
        } else if (var0 == 1) {
          if (da.a_da.a_cd.a_cf.c_B == 0) {
            if (da.a_da.a_cd.a_cf != null) {
              da.a_da.a_cd.a_cf.b();
              return;
            }
          } else if (da.a_da.a_cd.a_cf.c_B == cf.e_B) {
            if (da.a_da.a_cd.a_cf.a_S > 0 && da.a_da.a_cd.a_cf.a_S == da.a_da.a_cd.a_cf.b_B) {
              da.a_da.a_cd.a_cf.a_S--;
            }

            if (da.a_da.a_cd.a_cf.b_B > 0) {
              da.a_da.a_cd.a_cf.b_B--;
              return;
            }
          }
        }
      } else if (da.a_da.a_cd.b_B == 8) {
        if (var0 == b.a_S || var0 == b.b_S) {
          if (var0 == b.a_S) {
            o.c_I += 5;
            if (da.a_da.a_cd.a_AI[0] == 1) {
              o.b(da.a_da.a_cd.a_AI[1], da.a_da.a_cd.a_AI[2]);
            } else if (da.a_da.a_cd.a_AI[0] == 2) {
              o.d(da.a_da.a_cd.a_AI[1], -1);
            } else if (da.a_da.a_cd.a_AI[0] == 3) {
              o.a_I = o.a_I + da.a_da.a_cd.a_AI[1];
            } else if (da.a_da.a_cd.a_AI[0] == 4) {
              o.a_V(da.a_da.a_cd.a_AI[1], -1);
            }

            Object var6 = null;
            int var1 = 0;

            while (o.n_AString != null && var1 < o.n_AString.length && (var26 = cg.b_AI(o.n_AString[var1], ","))[0] != 5) {
              var1++;
            }

            if (var26 != null) {
              o.a_Abl[0].a_AI[1] = o.a_Abl[0].a_AI[1] + var26[1];

              for (int var11 = 0; o.a_Acv != null && var11 < o.a_Acv.length; var11++) {
                if (o.a_Acv[var11].a_Z) {
                  o.a_Acv[var11].a_AI[13] = o.a_Acv[var11].a_AI[13] + var26[1];
                }
              }
            }

            da.a_da.a_cd.a(o.a_Abl[0]);
            if (da.a_da.a_cd.n_Z) {
              da.a_da.a_cd.b_AString = cg.a(da.a_da.a_cd.b_AString, o.a_Abl[0].a_String + " đã lên cấp");
            }

            cd.a(o.a_Acv);
            da.a_da.a_cd.a(da.a_da.a_cd.b_AString);
            da.a_da.a_cd.b_AString = null;
          }

          da.a_da.a_cd.c_String = null;
          da.a_da.a_cd.b_B = 1;
          da.a_da.a_cd.a_ci.a(0);
          k.a(o.r_AS[0], o.r_AS[1], cg.a_S(o.m_AString[0]));
          return;
        }
      } else if (da.a_da.a_cd.b_B == 9) {
        if (da.a_da.a_cd.c_B == 1) {
          k.b();
          if (var0 == b.a_S) {
            da.a_da.a_cd.c_B = 2;
            return;
          }

          if (var0 != b.b_S) {
            return;
          }
        } else {
          if (var0 != b.a_S && var0 != b.b_S) {
            return;
          }

          if (var0 == b.a_S) {
            o.a(da.a_da.a_cd.g_S);
          }
        }

        da.a_da.a_cd.c_String = null;
        da.a_da.a_cd.a_ci.a(3);
        da.a_da.a_cd.b_B = 1;
      }
    }
  }

  public static void a() {
    if (da.a_da.a_cd.a_bn == null) {
      if (at.g_Z) {
        try {
          Thread.sleep(200L);
        } catch (InterruptedException var1) {
          var1.printStackTrace();
        }
      }

      as.a_bl = o.a_Abl[0];
      as.a_Acv = null;
      as.a_Acv = k.a_Acv();
      da.a_da.a_cd.a_as = new as();
      da.a_da.a_cd.b_B = 4;
      if (at.f_Z) {
        da.a_da.a_cd.e();
      }
    }
  }

  public static void b() {
    if (df.c_Z) {
      if (o.c_B == 0) {
        da.a_da.a("Hiện không thể dùng biến thân", 10);
      } else if (o.c_B == 1) {
        da.a_da.a("Hiện không thể dùng biến thân", 10);
      } else {
        da.a_da.a_cd.h_Z = false;
        da.a_da.a_cd.s_Z = true;
        o.a_Abl[0].j_Z = false;
        da.a_da.a_cd.h_B = 0;
      }
    } else {
      da.a_da.a_cd.a_AString = new String[]{"Chưa mở biến thân bay!"};
      da.a_da.a_cd.d_B = 0;
      da.a_da.a_cd.b_B = 3;
    }
  }

  public static void b(int var0) {
    if (var0 == 0) {
      if (o.a_Abl[0].b_Z) {
        o.a_Abl[0].j_V();
      } else {
        o.a_Abl[0].c_Z = false;
        o.a_Abl[0].i_V();
      }
    } else if (var0 == 1) {
      if (!o.a_Abl[0].c_Z) {
        o.a_Abl[0].c_B = 5;
        o.a_Abl[0].a_bx.a(o.g_String, 1);
        if (o.a_Abl[0].d_B == 0) {
          o.a_Abl[0].a_bx.a_V(0);
          o.a_Abl[0].a_bx.b(0);
        }

        o.a_Abl[0].c_Z = true;
        if (o.a_Abl[0].b_Z) {
          da.a_da.a_cd.a_bj.b(o.a_Abl[0]);
          o.a_Abl[0].b_Z = false;
          o.a_Abl[0].f_Z = false;
        }
      } else {
        o.a_Abl[0].c_B = 4;
        o.a_Abl[0].a_bx.a(o.e_AString[o.a_Abl[0].h_S - 1], 1);

        for (int var1 = 0; var1 < o.a_Abl.length; var1++) {
          if (var1 != 0) {
            o.a_Abl[var1].a(o.a_Abl[0].d_S, o.a_Abl[0].e_S);
            o.a_Abl[var1].j_Z = true;
          }
        }

        o.a_Abl[0].c_Z = false;
      }
    } else if (var0 == 2) {
      o.a_Abl[0].c_B = 2;
      o.a_Abl[0].a_bx.a(o.i_String, 1);
      if (o.a_Abl[0].d_B == 0) {
        o.a_Abl[0].a_bx.a_V(0);
        o.a_Abl[0].a_bx.b(0);
      }

      o.a_Abl[0].c_Z = true;
    }

    o.a_Abl[0].j_Z = true;
    da.a_da.a_cd.h_Z = true;
  }
}
