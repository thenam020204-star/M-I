import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;

public class ab {
  public Vector a_Vector;
  public int[] a_AI;
  int a_I;
  int b_I;
  int c_I;
  int d_I;
  int e_I;
  Image a_Image;
  int f_I;

  public ab() {
  }

  public static void a_V(int var0) {
    label707: {
      if (da.a_da.a_cd.a_B == 2) {
        if (da.a_da.a_cd.a_as == null) {
          da.a_da.a_cd.b_B = 1;
          return;
        }

        k.b();
        if (as.o_B == 1) {
          if (var0 != 8 && var0 != b.a_S) {
            if (var0 == b.b_S) {
              a_Z();
              return;
            }

            if (var0 == 2) {
              da.a_da.a_cd.a_as.a_de.f();
              byte var16 = (byte)((byte)da.a_da.a_cd.a_as.a_de.c() + 2);
              da.a_da.a_cd.a_as.a(var16);
              return;
            }

            if (var0 == 5) {
              da.a_da.a_cd.a_as.a_de.e();
              byte var15 = (byte)((byte)da.a_da.a_cd.a_as.a_de.c() + 2);
              da.a_da.a_cd.a_as.a(var15);
              return;
            }
          } else if ((var5 = (byte)((byte)da.a_da.a_cd.a_as.a_de.c() + 2)) != 2 && var5 != 4) {
            if (var5 == 3) {
              if (as.a_Acv != null) {
                break label707;
              }

              da.a_da.a_cd.a_as.a("Không có sủng vật", 10);
              as.o_B = 1;
            } else {
              if (var5 != 7 || o.a_Abp != null) {
                break label707;
              }

              da.a_da.a_cd.a_as.a("Không có nhiệm vụ", 10);
              as.o_B = 1;
            }
          } else {
            if (var5 != 4 || as.a_bl.a_Ad != null) {
              if (as.a_bl.b_String.endsWith(".av")) {
                as.c_bx = new bx();
                int var13 = as.a_bl.b_String.indexOf(".");
                String var14 = as.a_bl.b_String.substring(0, var13) + "s.av";
                as.c_bx.a(var14);
              }
              break label707;
            }

            da.a_da.a("Chưa học kỹ năng nào", 8);
            as.o_B = 1;
          }
        } else if (as.o_B == 2) {
          if (var0 == b.b_S) {
            if (da.a_da.a_cd.a_as.e_AI != null) {
              for (int var11 = 0; var11 < da.a_da.a_cd.a_as.e_AI.length; var11++) {
                as.a_bl.a_AI[16] = as.a_bl.a_AI[16] + da.a_da.a_cd.a_as.e_AI[var11];
              }

              int[] var12 = new int[]{0, 0, 0, 0};
              int[] var4 = new int[]{0, 0, 0, 0, 0};
              da.a_da.a_cd.a_as.e_AI = var12;
              da.a_da.a_cd.a_as.f_AI = var4;
            }

            if (da.a_da.a_cd.a_as.b_Z) {
              da.a_da.a_cd.a_as.b_Z = false;
              a_Z();
              da.a_da.a_cd.a_ci.a(5);
              return;
            }

            da.a_da.a_cd.a_as.a((byte)1, (byte)0);
            da.a_da.a_cd.a_as.f();
            return;
          }

          if (var0 == 6) {
            da.a_da.a_cd.a_as.w_I++;
            da.a_da.a_cd.a_as.w_I %= 4;
            return;
          }

          if (var0 == 1) {
            da.a_da.a_cd.a_as.w_I--;
            da.a_da.a_cd.a_as.w_I = (da.a_da.a_cd.a_as.w_I + 4) % 4;
            return;
          }

          if (var0 == 2) {
            if (da.a_da.a_cd.a_as.e_AI[da.a_da.a_cd.a_as.w_I] > 0) {
              da.a_da.a_cd.a_as.e_AI[da.a_da.a_cd.a_as.w_I]--;
              as.a_bl.a_AI[16]++;

              for (int var10 = 0; var10 < 5; var10++) {
                da.a_da.a_cd.a_as.f_AI[var10] = da.a_da.a_cd.a_as.f_AI[var10] - as.a_bl.a_AAB[da.a_da.a_cd.a_as.w_I][var10];
              }

              return;
            }
          } else if (var0 == 5) {
            if (as.a_bl.a_AI[16] > 0) {
              da.a_da.a_cd.a_as.e_AI[da.a_da.a_cd.a_as.w_I]++;
              as.a_bl.a_AI[16]--;

              for (int var9 = 0; var9 < 5; var9++) {
                da.a_da.a_cd.a_as.f_AI[var9] = da.a_da.a_cd.a_as.f_AI[var9] + as.a_bl.a_AAB[da.a_da.a_cd.a_as.w_I][var9];
              }

              return;
            }
          } else if (var0 == 8 || var0 == b.a_S) {
            for (int var7 = 0; var7 < 4; var7++) {
              as.a_bl.a_AI[var7 + 17] = as.a_bl.a_AI[var7 + 17] + da.a_da.a_cd.a_as.e_AI[var7];

              for (int var2 = 0; var2 < 5; var2++) {
                if (var2 < 2) {
                  as.a_bl.a_AI[4 + (var2 << 1)] = as.a_bl.a_AI[4 + (var2 << 1)] + da.a_da.a_cd.a_as.e_AI[var7] * as.a_bl.a_AAB[var7][var2];
                } else {
                  as.a_bl.a_AI[var2 + 5] = as.a_bl.a_AI[var2 + 5] + da.a_da.a_cd.a_as.e_AI[var7] * as.a_bl.a_AAB[var7][var2];
                }

                as.a_bl.a_AI[var2 + 10] = as.a_bl.a_AI[var2 + 10] + da.a_da.a_cd.a_as.e_AI[var7] * as.a_bl.a_AAB[var7][var2];
              }
            }

            int[] var8 = new int[]{0, 0, 0, 0};
            int[] var3 = new int[]{0, 0, 0, 0, 0};
            da.a_da.a_cd.a_as.e_AI = var8;
            da.a_da.a_cd.a_as.f_AI = var3;
            return;
          }
        } else {
          if (as.o_B == 3) {
            g(var0);
            return;
          }

          if (as.o_B == 14) {
            switch (as.A_B) {
              case 0:
                if (var0 != 8 && var0 != b.a_S) {
                  if (var0 == b.b_S) {
                    a_Z();
                    da.a_da.a_cd.a_ci.a(5);
                    return;
                  }

                  if (var0 == 1) {
                    as.a_ab.b_V();
                    return;
                  }

                  if (var0 == 6) {
                    as.a_ab.c_V();
                  }
                  break;
                }

                if (b_Z()) {
                  if (as.c_I == 4 && as.a_Acv == null) {
                    da.a_da.a_cd.a_as.a("Không có sủng vật", 10);
                  } else {
                    as.A_B = 1;
                    as.n_I = 0;
                    da.a_da.a_cd.a_as.g();
                    da.a_da.a_cd.a_as.a(0, da.a_da.a_cd.a_as.j_I - 2);
                  }

                  return;
                } else {
                  da.a_da.a_cd.a_as.a("Nghề này không thể học kỹ năng này", 12);
                  return;
                }
              case 1:
                if (var0 != 8 && var0 != b.a_S) {
                  if (var0 == b.b_S) {
                    as.A_B = 0;
                    as.m_I = 0;
                    da.a_da.a_cd.a_as.a(160, da.a_da.a_cd.a_as.j_I - 160);
                    return;
                  }

                  if (var0 == 1 && as.c_I == 4) {
                    if (as.n_I > 0 && as.n_I == as.m_I) {
                      as.n_I--;
                    }

                    if (as.m_I > 0) {
                      as.m_I--;
                      return;
                    }
                  } else if (var0 == 6 && as.c_I == 4) {
                    int var6 = 1;
                    if (as.c_I == 4) {
                      var6 = as.a_Acv.length;
                    }

                    if (as.m_I < var6 - 1) {
                      as.m_I++;
                    }

                    if (as.n_I + as.l_I == as.m_I) {
                      as.n_I++;
                    }
                  }
                  break;
                }

                if (a(as.c_I, as.m_I, as.a_ab.b_I())) {
                  da.a_da.a_cd.a_as.a("Đã học kỹ năng này", 10);
                  return;
                } else if (!a_Z(as.c_I, as.m_I)) {
                  if (as.c_I != 4) {
                    da.a_da.a_cd.a_as.a(" không đủ cấp yêu cầu", 10);
                  } else {
                    da.a_da.a_cd.a_as.a("Cấp sủng vật không đạt yêu cầu hoặc số kỹ năng đã tối đa", 10);
                  }

                  return;
                } else {
                  if (o.a_I < d.d_AS[o.c_I(as.a_ab.a_I(as.a_ab.b_I()))]) {
                    da.a_da.a_cd.a_as.a("Không đủ vàng‚không thể học kỹ năng", 10);
                  } else {
                    as.A_B = 2;
                  }

                  return;
                }
              case 2:
                if (var0 == 8 || var0 == b.a_S) {
                  if (as.c_I != 4) {
                    o.a(o.a_Abl[0], d.a_AS[o.c_I(as.a_ab.a_I(as.a_ab.b_I()))], 1);
                  } else {
                    o.a(as.a_Acv[as.m_I], d.a_AS[o.c_I(as.a_ab.a_I(as.a_ab.b_I()))], 1);
                  }

                  o.a_I = o.a_I - d.d_AS[o.c_I(as.a_ab.a_I(as.a_ab.b_I()))];
                  da.a_da.a_cd.a_as.a("Học thành công", 10);
                  as.m_I = 0;
                  as.A_B = 1;
                  as.n_I = 0;
                  break;
                } else {
                  if (var0 == b.b_S) {
                    as.m_I = 0;
                    as.n_I = 0;
                    as.A_B = 1;
                  }

                  return;
                }
              default:
                return;
            }
          } else {
            if (as.o_B == 9) {
              h(var0);
              return;
            }

            if (as.o_B == 5) {
              f(var0);
              return;
            }

            if (as.o_B == 6) {
              if (as.v_B == 0) {
                if (var0 == 8 || var0 == b.a_S) {
                  as.g_B = 0;
                  as.f_B = 0;
                  as.v_B = 1;
                } else if (var0 == b.b_S) {
                  da.a_da.a_cd.a_as.a((byte)1, (byte)0);
                  da.a_da.a_cd.a_as.f();
                } else if (var0 == 2) {
                  if (--as.e_B >= 0) {
                    return;
                  }

                  as.e_B = 2;
                } else {
                  if (var0 != 5 || ++as.e_B <= 2) {
                    return;
                  }

                  as.e_B = 0;
                }
              } else if (as.v_B == 1) {
                if (var0 != 8 && var0 != b.a_S) {
                  if (var0 == b.b_S) {
                    as.g_B = 0;
                    as.f_B = 0;
                    da.a_da.a_cd.a_as.a((byte)6, (byte)0);
                    da.a_da.a_cd.a_as.f();
                  } else if (var0 == 5) {
                    if (as.g_B < ct.a_S - 1) {
                      as.g_B++;
                    }

                    if (as.g_B - as.f_B * as.c_S < as.c_S * as.d_S) {
                      return;
                    }

                    as.f_B++;
                  } else if (var0 == 2) {
                    if (as.f_B > 0 && as.g_B == as.f_B * as.c_S) {
                      as.f_B--;
                    }

                    if (as.g_B <= 0) {
                      return;
                    }

                    as.g_B--;
                  } else if (var0 == 6) {
                    if (as.g_B + as.c_S <= ct.a_S - 1) {
                      as.g_B = (byte)(as.g_B + as.c_S);
                    } else if (as.g_B < ct.a_S - 1) {
                      as.g_B++;
                    }

                    if (as.g_B - as.f_B * as.c_S < as.c_S * as.d_S) {
                      return;
                    }

                    as.f_B++;
                  } else {
                    if (var0 != 1) {
                      return;
                    }

                    if (as.g_B - as.c_S >= 0) {
                      as.g_B = (byte)(as.g_B - as.c_S);
                    } else if (as.g_B > 0) {
                      as.g_B--;
                    }

                    if (as.f_B <= 0 || as.g_B >= as.f_B * as.c_S) {
                      return;
                    }

                    as.f_B--;
                  }
                } else {
                  if (as.a_AAAS == null || as.a_AAAS[as.e_B] == null || as.g_B >= as.a_AAAS[as.e_B].length || o.f_AS[o.d_I(as.a_AAAS[as.e_B][as.g_B][0])] == 3) {
                    return;
                  }

                  as.h_B = 0;
                  as.c_AString = new String[]{"Dùng", "Bán", "Bỏ"};
                  as.b_AI = new int[]{13, 14, 12};
                  if (as.e_B == 1) {
                    as.c_AString = new String[]{"Bán", "Bỏ"};
                    as.b_AI = new int[]{14, 12};
                  }

                  as.v_B = 2;
                }
              } else if (as.v_B == 2) {
                if (var0 == 8 || var0 == b.a_S) {
                  if (as.c_AString[as.h_B].equals("Dùng")) {
                    if (o.d_I(as.a_AAAS[as.e_B][as.g_B][0]) == 5) {
                      da.a_da.a_cd.a_as.a("Vật phẩm này chỉ dùng được trong trận", 10);
                      System.out.println("Cola vạn năng");
                    } else if (o.f_AS[o.d_I(as.a_AAAS[as.e_B][as.g_B][0])] == 3) {
                      da.a_da.a_cd.a_as.a("Không thể dùng vật phẩm này", 10);
                    } else if (o.f_AS[o.d_I(as.a_AAAS[as.e_B][as.g_B][0])] == 4) {
                      if (as.a_AAAS[as.e_B][as.g_B][0] == 14 && as.a_Acv != null && as.a_Acv.length >= ct.c_B) {
                        da.a_da.a_cd.a_as.a("Túi sủng vật đã đầy", 8);
                      } else {
                        o.b(o.a_Abl[0], as.a_AAAS[as.e_B][as.g_B][0]);
                        as.a_AAAS = null;
                        if (da.a_da.a_cd.a_as != null) {
                          as.j();
                          as.v_B = 1;
                          as.o_B = 6;
                        }
                      }
                    } else {
                      da.a_da.a_cd.a_as.a((byte)13, (byte)0);
                      as.m_B = 0;
                      as.n_B = 0;
                    }
                  } else if (as.c_AString[as.h_B].equals("Bán")) {
                    as.e_S = 1;
                    as.v_B = 3;
                  } else if (as.c_AString[as.h_B].equals("Bỏ")) {
                    as.e_S = as.a_AAAS[as.e_B][as.g_B][1];
                    as.v_B = 4;
                  } else {
                    if (!as.c_AString[as.h_B].equals("Hủy")) {
                      return;
                    }

                    as.v_B = 1;
                  }
                } else if (var0 == b.b_S) {
                  da.a_da.a_cd.a_as.a((byte)6, (byte)1);
                  da.a_da.a_cd.a_as.f();
                } else if (var0 == 6) {
                  if (as.h_B >= as.c_AString.length - 1) {
                    return;
                  }

                  as.h_B++;
                } else {
                  if (var0 != 1 || as.h_B <= 0) {
                    return;
                  }

                  as.h_B--;
                }
              } else if (as.v_B == 4) {
                if (var0 == 8 || var0 == b.a_S) {
                  if (o.f_AS[o.d_I(as.a_AAAS[as.e_B][as.g_B][0])] != 3) {
                    as.a_AAAS = a(as.a_AAAS, as.e_B, as.g_B, as.e_S);
                  } else {
                    da.a_da.a_cd.a_as.a("Không thể bỏ vật phẩm này!", 8);
                  }

                  as.e_S = 1;
                  as.v_B = 1;
                } else if (var0 == b.b_S) {
                  da.a_da.a_cd.a_as.a((byte)6, (byte)2);
                } else if (var0 == 5) {
                  if (++as.e_S <= as.a_AAAS[as.e_B][as.g_B][1]) {
                    return;
                  }

                  as.e_S = 1;
                } else {
                  if (var0 != 2 || --as.e_S > 0) {
                    return;
                  }

                  as.e_S = as.a_AAAS[as.e_B][as.g_B][1];
                }
              } else {
                if (as.v_B != 3) {
                  return;
                }

                if (var0 == 8 || var0 == b.a_S) {
                  if (o.f_AS[o.d_I(as.a_AAAS[as.e_B][as.g_B][0])] != 3) {
                    o.a_I = o.a_I + o.i_AS[o.d_I(as.a_AAAS[as.e_B][as.g_B][0])] * as.e_S;
                    as.a_AAAS = a(as.a_AAAS, as.e_B, as.g_B, as.e_S);
                  } else {
                    da.a_da.a_cd.a_as.a("Không thể bán vật phẩm này!", 8);
                  }

                  as.e_S = 1;
                  as.v_B = 1;
                } else if (var0 == b.b_S) {
                  da.a_da.a_cd.a_as.a((byte)6, (byte)2);
                } else {
                  if (var0 != 5) {
                    if (var0 == 2 && --as.e_S <= 0) {
                      as.e_S = as.a_AAAS[as.e_B][as.g_B][1];
                    }

                    return;
                  }

                  if (++as.e_S <= as.a_AAAS[as.e_B][as.g_B][1]) {
                    return;
                  }

                  as.e_S = 1;
                }
              }
            } else {
              if (as.o_B == 7) {
                e(var0);
                return;
              }

              if (as.o_B == 4) {
                if (as.b_S == 0) {
                  as.o_B = 1;
                } else {
                  if (as.x_B != 0) {
                    if (as.x_B == 1 && (var0 == 8 || var0 == b.a_S || var0 == b.b_S)) {
                      da.a_da.a_cd.a_as.a((byte)4, (byte)0);
                    }

                    return;
                  }

                  if (var0 == 8 || var0 == b.a_S) {
                    as.x_B = 1;
                  } else if (var0 == b.b_S) {
                    da.a_da.a_cd.a_as.d_ab.b(0);
                    da.a_da.a_cd.a_as.d_ab.d();
                    da.a_da.a_cd.a_as.a((byte)1, (byte)0);
                    da.a_da.a_cd.a_as.f();
                  } else if (var0 == 1) {
                    da.a_da.a_cd.a_as.d_ab.b_V();
                  } else {
                    if (var0 != 6) {
                      return;
                    }

                    da.a_da.a_cd.a_as.d_ab.c_V();
                  }
                }
              } else {
                if (as.o_B == 8) {
                  d(var0);
                  return;
                }

                if (as.o_B == 13) {
                  int var1 = 1;
                  if (as.a_Acv != null) {
                    var1 = 1 + as.a_Acv.length;
                  }

                  if (var0 != 8 && var0 != b.a_S) {
                    if (var0 == b.b_S) {
                      as.e_B = 0;
                      as.f_B = 0;
                      as.g_B = 0;
                      da.a_da.a_cd.a_as.a((byte)6, (byte)0);
                    } else {
                      if (var0 != 1) {
                        if (var0 == 6) {
                          if (as.m_B < var1 - 1) {
                            as.m_B++;
                          }

                          if (as.m_B - as.n_B == as.C_B) {
                            as.n_B++;
                            return;
                          }
                        }

                        return;
                      }

                      if (as.n_B > 0 && as.n_B == as.m_B) {
                        as.n_B--;
                      }

                      if (as.m_B <= 0) {
                        return;
                      }

                      as.m_B--;
                    }
                  } else {
                    if (as.m_B > 0) {
                      if (as.a_AAAS[as.e_B][as.g_B][0] == 52) {
                        if (o.p_AS == null) {
                          da.a_da.a_cd.a_as.a("Đồ giám không có hình dạng để chọn", 10);
                          return;
                        }

                        da.a_da.a_cd.a_as.a((byte)12, (byte)0);
                        return;
                      }

                      o.a_V(as.a_Acv[as.m_B - 1], as.a_AAAS[as.e_B][as.g_B][0]);
                    } else {
                      if (as.a_AAAS[as.e_B][as.g_B][0] == 52) {
                        da.a_da.a_cd.a_as.a("Không dùng cho nhân vật chính", 10);
                        return;
                      }

                      o.b(o.a_Abl[0], as.a_AAAS[as.e_B][as.g_B][0]);
                    }

                    as.a_AAAS = null;
                    as.j();
                    as.n_B = 0;
                    as.m_B = 0;
                    da.a_da.a_cd.a_as.a((byte)6, (byte)1);
                  }
                } else if (as.o_B == 15) {
                  switch (as.B_B) {
                    case 0:
                      if (var0 == 8 || var0 == b.a_S) {
                        as.B_B = 1;
                        return;
                      }

                      if (var0 == b.b_S) {
                        a_Z();
                        as.k_I = 1;
                        da.a_da.a_cd.a_ci.a(5);
                        return;
                      }

                      if (var0 == 1) {
                        as.b_ab.b_V();
                        return;
                      }

                      if (var0 == 6) {
                        as.b_ab.c_V();
                      }
                      break;
                    case 1:
                      c(var0);
                      return;
                    default:
                      return;
                  }
                } else if (as.o_B == 12) {
                  if (var0 == 8 || var0 == b.a_S) {
                    o.c(as.a_AAAS[as.e_B][as.g_B][0], 1);
                    as.a_Acv[as.m_B - 1].a_I = da.a_da.a_cd.a_as.a_bc.a_I();
                    da.a_da.a_cd.a_as.a(as.a_Acv[as.m_B - 1].a_String + " Đã đổi hình dạng", 10);
                    as.a_AAAS = null;
                    da.a_da.a_cd.a_as.a();
                    as.j();
                    da.a_da.a_cd.a_as.a((byte)6, (byte)1);
                    return;
                  }

                  if (var0 == b.b_S) {
                    as.e_B = 0;
                    as.f_B = 0;
                    as.g_B = 0;
                    da.a_da.a_cd.a_as.a((byte)6, (byte)0);
                    return;
                  }

                  if (var0 == 1) {
                    da.a_da.a_cd.a_as.a_bc.f();
                    as.c_bx = new bx();
                    as.c_bx = k.a(da.a_da.a_cd.a_as.a_bc.a_I(), true);
                    return;
                  }

                  if (var0 == 6) {
                    da.a_da.a_cd.a_as.a_bc.e();
                    as.c_bx = new bx();
                    as.c_bx = k.a(da.a_da.a_cd.a_as.a_bc.a_I(), true);
                    return;
                  }

                  if (var0 == 2) {
                    da.a_da.a_cd.a_as.a_bc.d_V();
                    as.c_bx = new bx();
                    as.c_bx = k.a(da.a_da.a_cd.a_as.a_bc.a_I(), true);
                    return;
                  }

                  if (var0 == 5) {
                    da.a_da.a_cd.a_as.a_bc.c();
                    as.c_bx = new bx();
                    as.c_bx = k.a(da.a_da.a_cd.a_as.a_bc.a_I(), true);
                  }
                }
              }
            }
          }
        }
      }

      return;
    }

    da.a_da.a_cd.a_as.a(var5, (byte)0);
  }

  private static void c(int var0) {
    if (var0 != 8 && var0 != b.a_S) {
      if (var0 == b.b_S) {
        as.B_B = 0;
      } else {
        if (var0 == 2) {
          if (as.k_I > 1) {
            as.k_I--;
            return;
          }

          as.k_I = 99;
          return;
        }

        if (var0 != 5) {
          return;
        }

        if (as.k_I < 99) {
          as.k_I++;
          return;
        }
      }

      as.k_I = 1;
    } else if (as.y_B == 0) {
      if (o.a_I < o.h_AS[o.d_I(as.b_ab.a_I(as.b_ab.b_I()))] * as.k_I) {
        da.a_da.a_cd.a_as.a("Không đủ vàng", 15);
        return;
      }

      if (o.a_AAS != null) {
        var0 = o.d_I(as.b_ab.a_I(as.b_ab.b_I()));
        if (o.a_I(o.e_AS[var0]) > ct.a_S) {
          da.a_da.a_cd.a_as.a("Túi đồ đã đầy", 10);
          return;
        }

        if (o.b_I(as.b_ab.a_I(as.b_ab.b_I())) + as.k_I > 99) {
          as.k_I = 99 - o.b_I(as.b_ab.a_I(as.b_ab.b_I()));
        }

        if (as.k_I <= 0) {
          da.a_da.a("Số vật phẩm đã đạt giới hạn 99", 10);
          as.k_I = 1;
          return;
        }

        o.b(as.b_ab.a_I(as.b_ab.b_I()), as.k_I);
        o.a_I = o.a_I - o.h_AS[o.d_I(as.b_ab.a_I(as.b_ab.b_I()))] * as.k_I;
        da.a_da.a_cd.a_as.a("Mua " + o.f_AString[o.d_I(as.b_ab.a_I(as.b_ab.b_I()))] + as.k_I, 12);
        as.k_I = 1;
        as.B_B = 0;
        return;
      }

      if (o.a_AAS == null) {
        if (o.b_I(as.b_ab.a_I(as.b_ab.b_I())) + as.k_I > 99) {
          as.k_I = 99 - o.b_I(as.b_ab.a_I(as.b_ab.b_I()));
        }

        if (as.k_I <= 0) {
          da.a_da.a("Số vật phẩm đã đạt giới hạn 99", 10);
          as.k_I = 1;
          return;
        }

        o.b(as.b_ab.a_I(as.b_ab.b_I()), as.k_I);
        o.a_I = o.a_I - o.h_AS[o.d_I(as.b_ab.a_I(as.b_ab.b_I()))] * as.k_I;
        da.a_da.a_cd.a_as.a("Mua " + o.f_AString[o.d_I(as.b_ab.a_I(as.b_ab.b_I()))] + as.k_I, 12);
        as.k_I = 1;
        as.B_B = 0;
        return;
      }
    } else if (as.y_B == 1) {
      if (o.a_I < ck.c_AS[o.f_I(as.b_ab.a_I(as.b_ab.b_I()))] * as.k_I) {
        da.a_da.a_cd.a_as.a("Không đủ vàng", 15);
        return;
      }

      if (o.b_AAS != null) {
        for (int var2 = 0; var2 < as.k_I; var2++) {
          o.d(as.b_ab.a_I(as.b_ab.b_I()), 1);
        }

        o.a_I = o.a_I - ck.c_AS[o.f_I(as.b_ab.a_I(as.b_ab.b_I()))] * as.k_I;
        da.a_da.a_cd.a_as.a("Mua " + ck.a_AString[o.f_I(as.b_ab.a_I(as.b_ab.b_I()))], 12);
        as.k_I = 1;
        as.B_B = 0;
        return;
      }
    }
  }

  private static boolean b_Z() {
    short[][] var0 = d.a_AAAS[o.c_I(as.a_ab.a_I(as.a_ab.b_I()))];
    int var1 = 0;

    for (int var2 = var0.length; var1 < var2; var1++) {
      if (var0[var1][0] == as.c_I) {
        return true;
      }
    }

    return false;
  }

  private static boolean a_Z(int var0, int var1) {
    short[][] var2 = d.a_AAAS[o.c_I(as.a_ab.a_I(as.a_ab.b_I()))];
    int var3 = -1;

    for (int var4 = 0; var4 < var2.length; var4++) {
      if (var2[var4][0] == var0) {
        var3 = var4;
        break;
      }
    }

    if (var3 != -1) {
      if (var0 != 4) {
        if (var2[var3][1] <= o.a_Abl[0].a_AI[0]) {
          return true;
        }
      } else if (var2[var3][1] <= as.a_Acv[var1].a_AI[0] && as.a_Acv[var1].a_AS.length <= 5) {
        return true;
      }
    }

    return false;
  }

  private static boolean a(int var0, int var1, int var2) {
    if (var0 != 4) {
      for (int var3 = 0; o.a_Abl[0].a_Ad != null && var3 < o.a_Abl[0].a_Ad.length; var3++) {
        if (o.a_Abl[0].a_Ad[var3].a_S == d.a_AS[o.c_I(as.a_AS[var2])]) {
          return true;
        }
      }
    } else {
      for (int var4 = 0; as.a_Acv[var1].a_Ad != null && var4 < as.a_Acv[var1].a_Ad.length; var4++) {
        if (as.a_Acv[var1].a_Ad[var4].a_S == d.a_AS[o.c_I(as.a_AS[var2])]) {
          return true;
        }
      }
    }

    return false;
  }

  private static void d(int var0) {
    if (as.z_B == 0) {
      if (var0 == b.b_S) {
        as.a_AB = null;
        as.b_AB = null;
        da.a_da.a_cd.a_as.a((byte)1, (byte)0);
        da.a_da.a_cd.a_as.f();
        return;
      }

      if (var0 != 8 && var0 != b.a_S) {
        if (var0 == 6) {
          if (da.a_da.a_cd.a_as.b_I == 2) {
            da.a_da.a_cd.a_as.b_bc.e();
            return;
          }

          if (da.a_da.a_cd.a_as.b_I == 0) {
            if (da.a_da.a_cd.a_as.d_bc != null) {
              da.a_da.a_cd.a_as.d_bc.e();
              return;
            }
          } else if (da.a_da.a_cd.a_as.b_I == 1 && da.a_da.a_cd.a_as.c_bc != null) {
            da.a_da.a_cd.a_as.c_bc.e();
            return;
          }
        } else if (var0 == 1) {
          if (da.a_da.a_cd.a_as.b_I == 2) {
            da.a_da.a_cd.a_as.b_bc.f();
            return;
          }

          if (da.a_da.a_cd.a_as.b_I == 0) {
            if (da.a_da.a_cd.a_as.d_bc != null) {
              da.a_da.a_cd.a_as.d_bc.f();
              return;
            }
          } else if (da.a_da.a_cd.a_as.b_I == 1 && da.a_da.a_cd.a_as.c_bc != null) {
            da.a_da.a_cd.a_as.c_bc.f();
            return;
          }
        } else if (var0 == 5) {
          if (da.a_da.a_cd.a_as.b_I == 2) {
            da.a_da.a_cd.a_as.b_bc.c();
            return;
          }

          if (da.a_da.a_cd.a_as.b_I == 0) {
            if (da.a_da.a_cd.a_as.d_bc != null) {
              da.a_da.a_cd.a_as.d_bc.c();
              return;
            }
          } else if (da.a_da.a_cd.a_as.b_I == 1 && da.a_da.a_cd.a_as.c_bc != null) {
            da.a_da.a_cd.a_as.c_bc.c();
            return;
          }
        } else if (var0 == 2) {
          if (da.a_da.a_cd.a_as.b_I == 2) {
            da.a_da.a_cd.a_as.b_bc.d_V();
            return;
          }

          if (da.a_da.a_cd.a_as.b_I == 0) {
            if (da.a_da.a_cd.a_as.d_bc != null) {
              da.a_da.a_cd.a_as.d_bc.d_V();
              return;
            }
          } else if (da.a_da.a_cd.a_as.b_I == 1 && da.a_da.a_cd.a_as.c_bc != null) {
            da.a_da.a_cd.a_as.c_bc.d_V();
            return;
          }
        } else {
          if (var0 == 55) {
            da.a_da.a_cd.a_as.b_I--;
            da.a_da.a_cd.a_as.b_I = (da.a_da.a_cd.a_as.b_I + 3) % 3;
            return;
          }

          if (var0 == 57) {
            da.a_da.a_cd.a_as.b_I++;
            da.a_da.a_cd.a_as.b_I %= 3;
            return;
          }
        }
      } else {
        if (da.a_da.a_cd.a_as.b_I == 2) {
          String var10 = da.a_da.a_cd.a_as.b_bc.a_String();
          int var13 = da.a_da.a_cd.a_as.b_bc.a_I();
          if (!var10.equals("") && !var10.equals("???")) {
            da.a_da.a_cd.a_as.a(var13);
            da.a_da.a_cd.a_as.i_I = var13;
            da.a_da.a_cd.a_as.a((byte)8, (byte)1);
          }

          return;
        }

        if (da.a_da.a_cd.a_as.b_I == 1) {
          String var6 = da.a_da.a_cd.a_as.c_bc.a_String();
          int var12 = da.a_da.a_cd.a_as.c_bc.a_I();
          if (!var6.equals("") && !var6.equals("???")) {
            as.a_AB = null;
            as.b_AB = null;
            as.c_bx = new bx();
            as.c_bx = k.a(var12, false);
            String[] var15;
            short[][] var20 = new short[(var15 = cg.b_AString(as.a_AAString[var12 - 1][19], "|")).length][2];

            for (int var23 = 0; var23 < var15.length; var23++) {
              short[] var7 = cg.b_AS(var15[var23], ",");
              var20[var23][0] = var7[0];
              var20[var23][1] = var7[1];
            }

            da.a_da.a_cd.a_as.b_AAS = var20;
            if (as.a_AAString[var12 - 1][2] != null) {
              for (int var24 = 0; var24 < 4; var24++) {
                String var8;
                if ((var8 = as.a_AAString[var12 - 1][2].substring(var24, var24 + 1)).equals("a")) {
                  as.a_AB = cg.a_AB(as.a_AB, 10);
                } else {
                  as.a_AB = cg.a_AB(as.a_AB, cg.a_I(var8));
                }
              }
            }

            for (int var9 = 0; var9 < 5; var9++) {
              byte[] var25 = cg.b_AB(as.a_AAString[var12 - 1][4 + var9 * 2], "|");
              as.b_AB = cg.a_AB(as.b_AB, var25[0]);
            }

            if (o.a_AZ[var12 - 1]) {
              o.a_AZ[var12 - 1] = false;
            }

            as.a_I = var12 - 1;
            da.a_da.a_cd.a_as.i_I = var12;
            da.a_da.a_cd.a_as.a((byte)8, (byte)1);
          }

          return;
        }

        if (da.a_da.a_cd.a_as.b_I == 0 && da.a_da.a_cd.a_as.d_bc != null) {
          var0 = da.a_da.a_cd.a_as.d_bc.a_I();
          as.a_AB = null;
          as.b_AB = null;
          as.c_bx = new bx();
          as.c_bx = k.a(var0, false);
          if (as.a_AAString == null) {
            as.a_AAString = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
          }

          String[] var1;
          short[][] var2 = new short[(var1 = cg.b_AString(as.a_AAString[var0 - 1][19], "|")).length][2];

          for (int var3 = 0; var3 < var1.length; var3++) {
            short[] var4 = cg.b_AS(var1[var3], ",");
            var2[var3][0] = var4[0];
            var2[var3][1] = var4[1];
          }

          da.a_da.a_cd.a_as.b_AAS = var2;
          if (as.a_AAString[var0 - 1][2] != null) {
            for (int var18 = 0; var18 < 4; var18++) {
              String var21;
              if ((var21 = as.a_AAString[var0 - 1][2].substring(var18, var18 + 1)).equals("a")) {
                as.a_AB = cg.a_AB(as.a_AB, 10);
              } else {
                as.a_AB = cg.a_AB(as.a_AB, cg.a_I(var21));
              }
            }
          }

          for (int var22 = 0; var22 < 5; var22++) {
            byte[] var19 = cg.b_AB(as.a_AAString[var0 - 1][4 + var22 * 2], "|");
            as.b_AB = cg.a_AB(as.b_AB, var19[0]);
          }

          if (o.a_AZ[var0 - 1]) {
            o.a_AZ[var0 - 1] = false;
          }

          System.out.println("petID" + var0);
          as.a_I = var0 - 1;
          da.a_da.a_cd.a_as.i_I = var0;
          da.a_da.a_cd.a_as.a((byte)8, (byte)1);
          return;
        }
      }
    } else if (as.z_B == 1) {
      if (var0 == b.b_S) {
        if (!da.a_da.a_cd.a_as.a_Z) {
          da.a_da.a_cd.a_as.a((byte)8, (byte)0);
          return;
        }

        da.a_da.a_cd.a_as.a_Z = false;
        a_Z();
        da.a_da.a_cd.a_ci.a(5);
        return;
      }

      if (var0 == b.a_S) {
        da.a_da.a_cd.a_as.a((byte)8, (byte)2);
        return;
      }
    } else if (as.z_B == 2 && !da.a_da.a_cd.a_as.f_Z) {
      if (var0 == b.b_S) {
        if (!da.a_da.a_cd.a_as.a_Z) {
          da.a_da.a_cd.a_as.a((byte)8, (byte)1);
          return;
        }

        da.a_da.a_cd.a_as.a_Z = false;
        a_Z();
        da.a_da.a_cd.a_ci.a(5);
        return;
      }

      if (var0 == b.a_S) {
        short[][] var11 = da.a_da.a_cd.a_as.b_AAS;
        boolean var14 = true;
        if (var11 != null) {
          for (int var16 = 0; var16 < var11.length; var16++) {
            if (o.b_I(var11[var16][0]) < var11[var16][1]) {
              var14 = false;
            }
          }
        }

        if (!var14) {
          da.a_da.a_cd.a_as.a("Không đủ nguyên liệu", 10);
          return;
        }

        if (as.a_Acv != null && as.a_Acv.length >= ct.c_B) {
          da.a_da.a_cd.a_as.a("Ô sủng vật đã đầy", 8);
          return;
        }

        for (int var17 = 0; var17 < var11.length; var17++) {
          o.c(var11[var17][0], var11[var17][1]);
        }

        da.a_da.a_cd.a_as.f_Z = true;
        da.a_da.a_cd.a_as.e_Z = false;
        da.a_da.a_cd.a_as.o_I = 0;
      } else {
        if (var0 == 1) {
          da.a_da.a_cd.a_as.a_db.f();
          return;
        }

        if (var0 == 6) {
          da.a_da.a_cd.a_as.a_db.e();
        }
      }
    }
  }

  private static void e(int var0) {
    if (as.w_B == 0) {
      if (var0 == 8 || var0 == b.a_S) {
        if (as.i_B == 1) {
          if (da.a_da.a_cd.a_as.a_bf == null || da.a_da.a_cd.a_as.a_bf.b() == 0) {
            da.a_da.a_cd.a_as.a("Hiện chưa nhận nhiệm vụ phụ tuyến", 10);
            return;
          }
        } else if (as.i_B == 0 && (da.a_da.a_cd.a_as.a_cy == null || da.a_da.a_cd.a_as.a_cy.b() == 0)) {
          da.a_da.a_cd.a_as.a("Hiện chưa nhận nhiệm vụ chính tuyến", 10);
          return;
        }

        da.a_da.a_cd.a_as.a((byte)7, (byte)(as.i_B + 1));
        return;
      }

      if (var0 == b.b_S) {
        as.b_AString = null;
        as.i_B = 0;
        da.a_da.a_cd.a_as.a((byte)1, (byte)0);
        da.a_da.a_cd.a_as.f();
        return;
      }

      if (var0 == 5) {
        as.i_B = (byte)(as.i_B == 1 ? 0 : 1);
        return;
      }

      if (var0 == 2) {
        as.i_B = (byte)(as.i_B == 0 ? 1 : 0);
        return;
      }
    } else if (as.w_B != 1 && as.w_B != 2) {
      if (as.w_B == 3) {
        if (var0 == 8 || var0 == b.a_S || var0 == b.b_S) {
          as.w_B = (byte)(as.i_B + 1);
          return;
        }

        if (var0 == 6) {
          cs var8 = da.a_da.a_cd.a_as.a_cs;
          if (da.a_da.a_cd.a_as.a_cs.c_I < var8.b_I - var8.a_I) {
            var8.c_I++;
          }

          return;
        }

        if (var0 == 1) {
          cs var7 = da.a_da.a_cd.a_as.a_cs;
          if (da.a_da.a_cd.a_as.a_cs.c_I > 0) {
            var7.c_I--;
          }

          return;
        }
      } else if (as.w_B == 4) {
        if (var0 == 8 || var0 == b.a_S) {
          if (as.k_B == 0) {
            da.a_da.a_cd.a_as.p_I = da.a_da.a_cd.a_as.a_bf.a_I();
            da.a_da.a_cd.a_as.a((byte)7, (byte)3);
            return;
          }

          da.a_da.a_cd.a_as.p_I = da.a_da.a_cd.a_as.a_bf.a_I();
          as.w_B = 5;
          Object var11 = null;
          bf var6 = da.a_da.a_cd.a_as.a_bf;
          da.a_da.a_cd.a_as.a_bf.a_Image = (Image)var11;
          return;
        }

        if (var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)7, (byte)2);
          return;
        }

        if (var0 == 6) {
          if (as.k_B <= 0) {
            as.k_B++;
            return;
          }
        } else if (var0 == 1 && as.k_B > 0) {
          as.k_B--;
          return;
        }
      } else if (as.w_B == 5) {
        if (var0 == 8 || var0 == b.a_S) {
          short var4 = o.a_Abp[da.a_da.a_cd.a_as.p_I].a_S;
          o.a(o.a_Abp[da.a_da.a_cd.a_as.p_I]);
          o.n_AS = cg.b(o.n_AS, var4);
          da.a_da.a_cd.a_as.b();
          as.b_AString = cg.a(as.b_AString, 0);
          if (da.a_da.a_cd.a_as.a_bf != null && da.a_da.a_cd.a_as.a_bf.b() != 0) {
            Image var10 = da.a_da.a_cd.a_as.a_Image;
            bf var5 = da.a_da.a_cd.a_as.a_bf;
            da.a_da.a_cd.a_as.a_bf.a_Image = var10;
            as.w_B = 2;
            return;
          }

          da.a_da.a_cd.a_as.a((byte)7, (byte)0);
        } else if (var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)7, (byte)2);
        }
      }
    } else if (var0 != 8 && var0 != b.a_S) {
      if (var0 == b.b_S) {
        as.j_B = 0;
        da.a_da.a_cd.a_as.a((byte)7, (byte)0);
        da.a_da.a_cd.a_as.f();
        return;
      }

      if (var0 == 1) {
        if (as.w_B == 1) {
          da.a_da.a_cd.a_as.a_cy.f();
          return;
        }

        da.a_da.a_cd.a_as.a_bf.f();
        return;
      }

      if (var0 == 6) {
        if (as.w_B == 1) {
          da.a_da.a_cd.a_as.a_cy.e();
          return;
        }

        da.a_da.a_cd.a_as.a_bf.e();
        return;
      }
    } else {
      if (as.w_B != 2) {
        if (da.a_da.a_cd.a_as.a_cy.a_I() == 1) {
          da.a_da.a_cd.a_as.a("Đã nộp nhiệm vụ hoàn thành!", 10);
          return;
        }

        boolean var3 = false;

        for (int var9 = 0; o.a_Abp != null && var9 < o.a_Abp.length; var9++) {
          if (o.a_Abp[var9].a_String.equals(da.a_da.a_cd.a_as.a_cy.a_String()) && da.a_da.a_cd.a_as.a_cy.a_I() != 1) {
            da.a_da.a_cd.a_as.p_I = var9;
            var3 = true;
            break;
          }
        }

        if (var3) {
          da.a_da.a_cd.a_as.a((byte)7, (byte)3);
        }

        return;
      }

      boolean var2 = false;

      for (int var1 = 0; var1 < o.a_Abp.length; var1++) {
        if (o.a_Abp[var1].a_String.equals(da.a_da.a_cd.a_as.a_bf.a_String())) {
          if (o.a_Abp[var1].b_B == 1) {
            var2 = true;
          }
          break;
        }
      }

      if (!var2) {
        da.a_da.a_cd.a_as.p_I = da.a_da.a_cd.a_as.a_bf.a_I();
        da.a_da.a_cd.a_as.a((byte)7, (byte)3);
        return;
      }

      as.w_B = 4;
      da.a_da.a_cd.a_as.p_I = da.a_da.a_cd.a_as.a_bf.a_I();
    }
  }

  private static short[][][] a(short[][][] var0, int var1, int var2, int var3) {
    if (var3 <= 0) {
      return var0;
    } else {
      o.c(var0[var1][var2][0], var3);
      if (var0[var1][var2][1] > var3) {
        var0[var1][var2][1] = (short)(var0[var1][var2][1] - var3);
      } else {
        var0[var1] = cg.a(var0[var1], var2);
      }

      return var0;
    }
  }

  private static void f(int var0) {
    if (as.t_B == 0) {
      if (var0 != 8 && var0 != b.a_S) {
        if (var0 == b.b_S) {
          as.b_B = 0;
          da.a_da.a_cd.a_as.a((byte)1, (byte)0);
          da.a_da.a_cd.a_as.f();
          return;
        }

        if (var0 == 6) {
          if (as.b_B < 11) {
            as.b_B++;
            return;
          }
        } else if (var0 == 1) {
          if (as.b_B > 0) {
            as.b_B--;
            return;
          }
        } else if (var0 == 5) {
          if (as.b_B >= 5 && as.b_B < 11) {
            as.b_B++;
          }

          if (as.b_B < 3) {
            as.b_B = (byte)(as.b_B + 3);
            return;
          }

          if (as.b_B == 3) {
            as.b_B = 1;
            return;
          }

          if (as.b_B == 4) {
            as.b_B = 2;
            return;
          }
        } else if (var0 == 2) {
          if (as.b_B > 5) {
            as.b_B--;
            return;
          }

          if (as.b_B >= 3 && as.b_B <= 5) {
            as.b_B = (byte)(as.b_B - 3);
            return;
          }

          if (as.b_B == 2) {
            as.b_B = 4;
            return;
          }

          if (as.b_B == 1) {
            as.b_B = 3;
            return;
          }
        }
      } else if (as.b_B < 6) {
        if (as.a_bl.a_Ack[as.b_B] == null && o.a_AAck[as.b_B] == null) {
          String[] var7 = new String[]{"Vũ khí", "Mũ", "Áo", "Giày", "Dây chuyền", "Nhẫn"};
          da.a_da.a_cd.a_as.a("Không có " + var7[as.b_B], 10);
          return;
        }

        as.a_S = 0;
        as.c_B = 0;
        as.t_B = 1;
        return;
      }
    } else if (as.t_B == 1) {
      if (var0 == 8 || var0 == b.a_S) {
        as.d_B = 0;
        as.d_AString = new String[]{"Thay", "Tháo", "Bỏ", "Khảm"};
        as.c_AI = new int[]{10, 9, 12, 11};
        if (o.a_AAck[as.b_B] == null || as.a_S >= o.a_AAck[as.b_B].length) {
          as.d_AString = new String[]{"Tháo"};
          as.c_AI = new int[]{9};
        } else if (as.a_bl.a_Ack[as.b_B] == null) {
          as.d_AString = new String[]{"Trang bị ", "Khảm", "Bỏ"};
          as.c_AI = new int[]{8, 11, 12};
        }

        as.t_B = 2;
        return;
      }

      if (var0 == b.b_S) {
        da.a_da.a_cd.a_as.a((byte)5, (byte)0);
        da.a_da.a_cd.a_as.f();
        return;
      }

      if (var0 == 5 || var0 == 2 || var0 == 6 || var0 == 1) {
        if (var0 == 5) {
          if (as.a_S < ct.a_S - 1) {
            as.a_S++;
          }

          if (as.a_S - as.c_B * as.f_S >= as.f_S * as.g_S) {
            as.c_B++;
            return;
          }
        } else if (var0 == 2) {
          if (as.c_B > 0 && as.a_S == as.c_B * as.f_S) {
            as.c_B--;
          }

          if (as.a_S > 0) {
            as.a_S--;
            return;
          }
        } else if (var0 == 6) {
          if (as.a_S + as.f_S <= ct.a_S - 1) {
            as.a_S = (short)(as.a_S + as.f_S);
          } else if (as.a_S < ct.a_S - 1) {
            as.a_S++;
          }

          if (as.a_S - as.c_B * as.f_S >= as.f_S * as.g_S) {
            as.c_B++;
            return;
          }
        } else if (var0 == 1) {
          if (as.a_S - as.f_S >= 0) {
            as.a_S = (short)(as.a_S - as.f_S);
          } else if (as.a_S > 0) {
            as.a_S--;
          }

          if (as.c_B > 0 && as.a_S < as.c_B * as.f_S) {
            as.c_B--;
            return;
          }
        }
      }
    } else if (as.t_B == 2) {
      if (var0 != 8 && var0 != b.a_S) {
        if (var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)5, (byte)1);
          da.a_da.a_cd.a_as.f();
          return;
        }

        if (var0 == 1) {
          if (as.d_B > 0) {
            as.d_B--;
            return;
          }
        } else if (var0 == 6 && as.d_B < as.d_AString.length - 1) {
          as.d_B++;
          return;
        }
      } else {
        if (as.d_AString[as.d_B].equals("Thay")) {
          a(as.a_bl, as.b_B, as.a_S);
          as.t_B = 1;
          return;
        }

        if (as.d_AString[as.d_B].equals("Tháo")) {
          byte var8 = as.b_B;
          bl var5 = as.a_bl;
          if (as.a_bl.b_AS[var8] > 0) {
            o.b_AAS[var8] = cg.a_AS(o.b_AAS[var8], var5.b_AS[var8]);
            o.a_AAck[var8] = o.a(o.a_AAck[var8], var5.a_Ack[var8]);
            short var2 = var5.b_AS[var8];
            var5.b_AS[var8] = 0;
            var5.a_Ack[var8] = null;
            o.d(var5);
            if (cg.a_Z(as.b_AS, var2)) {
              as.q_I--;
            }

            a(var5, as.q_I);
          }

          as.t_B = 1;
          return;
        }

        if (as.d_AString[as.d_B].equals("Trang bị ")) {
          a(as.a_bl, as.b_B, as.a_S);
          as.t_B = 1;
          return;
        }

        if (as.d_AString[as.d_B].equals("Bỏ")) {
          o.b_AAS[as.b_B] = cg.c(o.b_AAS[as.b_B], as.a_S);
          o.a_AAck[as.b_B] = o.a(o.a_AAck[as.b_B], as.a_S);
          as.t_B = 1;
          return;
        }

        if (as.d_AString[as.d_B].equals("Khảm")) {
          if (o.f_AS == null) {
            o.d();
            o.e();
          }

          if (o.a_AAck[as.b_B][as.a_S].m_S == 0) {
            da.a_da.a_cd.a_as.a("Trang bị này không thể khảm", 8);
            return;
          }

          if (o.a_AAck[as.b_B][as.a_S].a_B >= o.a_AAck[as.b_B][as.a_S].m_S) {
            da.a_da.a_cd.a_as.a("Hết lượt khảm", 8);
            return;
          }

          boolean var4 = false;
          as.a_AAS = null;

          for (int var1 = 0; o.a_AAS != null && var1 < o.a_AAS.length; var1++) {
            if (o.f_AS[o.d_I(o.a_AAS[var1][0])] == 5 && o.h_AString[o.d_I(o.a_AAS[var1][0])] != "" && !o.h_AString[o.d_I(o.a_AAS[var1][0])].equals("-1")) {
              as.a_AAS = cg.a(as.a_AAS, new short[]{o.a_AAS[var1][0], o.a_AAS[var1][1]});
              var4 = true;
            }
          }

          if (!var4) {
            da.a_da.a_cd.a_as.a("Không có đá quý", 8);
            return;
          }

          as.e_I = 0;
          as.d_I = 0;
          as.g_I = 0;
          da.a_da.a_cd.a_as.a((byte)5, (byte)3);
          as.u_B = 0;
          return;
        }
      }
    } else if (as.t_B == 3) {
      if (as.u_B == 0) {
        if (var0 == 8) {
          if (as.a_AAS == null) {
            return;
          }

          var0 = as.a_AAS.length;
          if (as.g_I > 0) {
            short var9 = as.a_AAS[as.d_I][0];
            boolean var12 = false;

            for (int var3 = 0; var3 < as.a_AAS.length; var3++) {
              if (as.a_AAS[var3][0] == as.g_I) {
                as.a_AAS[var3][1]++;
                var12 = true;
                break;
              }
            }

            if (!var12) {
              short[][] var13 = new short[as.a_AAS.length + 1][2];
              System.arraycopy(as.a_AAS, 0, var13, 0, as.a_AAS.length);
              var13[var13.length - 1][0] = (short)as.g_I;
              var13[var13.length - 1][1] = 1;
              as.a_AAS = var13;
            }

            as.g_I = var9;
          } else {
            as.g_I = as.a_AAS[as.d_I][0];
          }

          for (int var10 = 0; var10 < as.a_AAS.length; var10++) {
            if (as.a_AAS[var10][0] == as.g_I) {
              as.a_AAS[var10][1]--;
              if (as.a_AAS[var10][1] <= 0) {
                as.a_AAS = cg.a(as.a_AAS, var10);
              }
              break;
            }
          }

          int var11 = 0;
          if (as.a_AAS != null) {
            var11 = as.a_AAS.length;
          }

          if (var11 < var0 && as.a_AAS != null) {
            if (as.e_I > 0) {
              as.e_I--;
            }

            if (as.d_I >= as.a_AAS.length - 1) {
              as.d_I--;
            }
          }

          return;
        }

        if (var0 == b.a_S) {
          if (as.g_I > 0) {
            as.g_Z = true;
            as.a_J = System.currentTimeMillis();
            return;
          }

          da.a_da.a_cd.a_as.a("Chưa chọn nguyên liệu", 8);
          return;
        }

        if (var0 == b.b_S) {
          as.g_I = 0;
          as.a_AAS = null;
          da.a_da.a_cd.a_as.a((byte)5, (byte)1);
          da.a_da.a_cd.a_as.f();
          return;
        }

        if (var0 == 1) {
          if (as.a_AAS == null) {
            return;
          }

          if (as.d_I == as.e_I && as.e_I > 0) {
            as.e_I--;
          }

          if (as.d_I > 0) {
            as.d_I--;
            return;
          }
        } else if (var0 == 6) {
          if (as.a_AAS == null) {
            return;
          }

          if (as.d_I < as.a_AAS.length - 1) {
            as.d_I++;
          }

          if (as.d_I == as.e_I + as.f_I) {
            as.e_I++;
            return;
          }
        }
      } else if (as.u_B == 1 && (var0 == b.a_S || var0 == b.b_S)) {
        as.g_I = 0;
        as.d_I = 0;
        as.e_I = 0;
        da.a_da.a_cd.a_as.a((byte)5, (byte)1);
        as.u_B = 0;
      }
    }
  }

  private static void a(bl var0, int var1, int var2) {
    String[] var3 = new String[]{"Chiến binh", "Chú thuật sư", "Pháp sư"};
    ck var5 = o.a_AAck[var1][var2];
    bl var4 = var0;
    boolean var6 = false;
    if (ck.b_AString != null) {
      ck.a_V();
    }

    short[] var12 = cg.b_AS(ck.b_AString[o.f_I(var5.a_S)], ",");

    for (int var7 = 0; var7 < var12.length; var7++) {
      if (var12[var7] == 0 || var12[var7] == var4.h_S) {
        var6 = true;
        break;
      }
    }

    if (!var6) {
      da.a_da.a_cd.a_as.a("Nhân vật không phù hợp", 8);
    } else {
      var5 = o.a_AAck[var1][var2];
      var6 = false;
      if (ck.d_AS == null) {
        ck.a_V();
      }

      short[] var14 = ck.d_AS;
      boolean var8 = false;
      short var10 = var14[o.f_I(var5.a_S)];
      if (df.b_Z || var0.a_AI[0] >= var10) {
        var8 = true;
      }

      if (var8) {
        var6 = true;
      }

      if (var6) {
        short var11 = var0.b_AS[var1];
        var5 = var0.a_Ack[var1];
        var0.b_AS[var1] = o.b_AAS[var1][var2];
        var0.a_Ack[var1] = new ck();
        var0.a_Ack[var1] = o.a_AAck[var1][var2];
        o.b_AAS[var1] = cg.c(o.b_AAS[var1], var2);
        o.a_AAck[var1] = o.a(o.a_AAck[var1], var2);
        if (var11 > 0) {
          o.b_AAS[var1] = cg.a_AS(o.b_AAS[var1], var11);
          o.a_AAck[var1] = o.a(o.a_AAck[var1], var5);
        }

        o.d(var0);
        if (cg.a_Z(as.b_AS, var0.b_AS[var1])) {
          da.a_da.a_cd.a_as.a("Trang bị " + var3[var0.h_S - 1] + " bộ [ " + ck.a_AString[o.f_I(var0.b_AS[var1])] + " ]", 8);
          if ((var11 <= 0 || cg.a_Z(as.b_AS, var11)) && var11 > 0) {
            return;
          }

          as.q_I++;
        } else if (var11 > 0) {
          if (cg.a_Z(as.b_AS, var11)) {
            as.q_I--;
          }

          a(var0, as.q_I);
          return;
        }

        a(var0, as.q_I);
      } else {
        da.a_da.a_cd.a_as.a("Cấp không đủ!", 8);
      }
    }
  }

  private static void a(bl var0, int var1) {
    if (var1 >= 2) {
      if (var0.h_S == 1) {
        var0.a_AI[13] = var0.a_AI[13] + 300;
      } else if (var0.h_S == 2) {
        var0.a_AI[10] = var0.a_AI[10] + 500;
      }
    }

    if (var1 >= 4) {
      if (var0.h_S == 1) {
        var0.a_AI[11] = var0.a_AI[11] + 400;
      } else if (var0.h_S == 2) {
        var0.a_AI[14] = var0.a_AI[14] + 300;
      }
    }

    if (var1 >= 6) {
      if (var0.h_S == 1) {
        var0.a_AI[10] = var0.a_AI[10] + 600;
        return;
      }

      if (var0.h_S == 2) {
        var0.a_AI[12] = var0.a_AI[12] + 500;
      }
    }
  }

  private static void g(int var0) {
    if (as.a_Acv == null) {
      da.a_da.a_cd.a_as.a((byte)1, (byte)0);
      da.a_da.a_cd.a_as.f();
    } else {
      if (as.q_B == 0) {
        if (var0 == 8 || var0 == b.a_S) {
          as.e_AString = new String[]{"Thuộc tính", "Kỹ năng", "Xuất chiến", "Thả", "Đổi tên", "Cấy", "Tinh luyện"};
          as.d_AI[2] = 2;
          if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_Z) {
            as.e_AString = new String[]{"Thuộc tính", "Kỹ năng", "Nghỉ", "Thả", "Đổi tên", "Cấy", "Tinh luyện"};
            as.d_AI[2] = 3;
          }

          as.a_B = 0;
          da.a_da.a_cd.a_as.a((byte)3, (byte)1);
          return;
        }

        if (var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)1, (byte)0);
          da.a_da.a_cd.a_as.f();
          return;
        }

        if (var0 == 1) {
          da.a_da.a_cd.a_as.a_l.f();
          da.a_da.a_cd.a_as.a_bx = new bx();
          da.a_da.a_cd.a_as.a_bx = k.a(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_I, true);
          return;
        }

        if (var0 == 6) {
          da.a_da.a_cd.a_as.a_l.e();
          da.a_da.a_cd.a_as.a_bx = new bx();
          da.a_da.a_cd.a_as.a_bx = k.a(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_I, true);
          return;
        }
      } else if (as.q_B == 1) {
        if (var0 != 8 && var0 != b.a_S) {
          if (var0 == b.b_S) {
            da.a_da.a_cd.a_as.a((byte)3, (byte)0);
            return;
          }

          if (var0 == 1) {
            if (as.a_B > 0) {
              as.a_B--;
              return;
            }
          } else if (var0 == 6 && as.a_B < as.e_AString.length - 1) {
            as.a_B++;
            return;
          }
        } else if (as.e_AString[as.a_B].equals("Xuất chiến")) {
          if (k.a_I() >= 2) {
            as.b_String = "Số sủng vật xuất chiến đã đủ! Thao tác thất bại!";
            da.a_da.a_cd.a_as.a("Số sủng vật xuất chiến đã đủ! Thao tác thất bại!", 8);
            return;
          }

          as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_Z = true;
          da.a_da.a_cd.a_as.a((byte)3, (byte)0);
        } else {
          if (as.e_AString[as.a_B].equals("Thuộc tính")) {
            var0 = 1;
            byte[][] var31 = as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB;
            String var26 = "Cấy";
            byte[][] var23 = var31;
            if (var31 == null) {
              System.out.println(var26 + " is null!");
            } else {
              System.out.println(var26 + "={");

              for (int var29 = 0; var29 < var23.length; var29++) {
                var26 = "  {";

                for (int var30 = 0; var23[var29] != null && var30 < var23[var29].length; var30++) {
                  if (var30 < var23[var29].length - 1) {
                    var26 = var26 + "" + var23[var29][var30] + ",";
                  } else {
                    var26 = var26 + "" + var23[var29][var30];
                  }
                }

                var26 = var26 + "}";
                if (var29 < var23.length - 1) {
                  System.out.println(var26 + ",");
                } else {
                  System.out.println(var26);
                }
              }

              System.out.println("}");
              System.out.println("length=" + var23.length);
            }

            for (int var24 = 0; var24 < 2; var24++) {
              if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var24][0] != 0) {
                var0++;
              }
            }

            da.a_da.a_cd.a_as.u_I = var0;
            da.a_da.a_cd.a_as.a((byte)3, (byte)2);
            return;
          }

          if (as.e_AString[as.a_B].equals("Nghỉ")) {
            as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_Z = false;
            da.a_da.a_cd.a_as.a((byte)3, (byte)0);
            return;
          }

          if (as.e_AString[as.a_B].equals("Kỹ năng")) {
            if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_Ad == null) {
              da.a_da.a_cd.a_as.a("Chưa học kỹ năng nào", 8);
              return;
            }

            da.a_da.a_cd.a_as.a((byte)3, (byte)5);
            d.a();
            d.b();
            return;
          }

          if (as.e_AString[as.a_B].equals("Thả")) {
            as.q_B = 3;
            return;
          }

          if (as.e_AString[as.a_B].equals("Đổi tên")) {
            ba var10000 = new ba();
            as var13 = da.a_da.a_cd.a_as;
            byte var4 = 4;
            boolean var2 = false;
            String var3 = "1-4 ký tự";
            String var25 = "Đổi tên sủng vật";
            ba var22 = var10000;
            var10000.a_cx = var13;
            var22.a_Form = null;
            var22.a_TextField = null;
            var22.a_Form = new Form(var25);
            var22.a_TextField = new TextField(var3, "", 4, 0);
            var22.a_Form.append(var22.a_TextField);
            var22.a_Form.addCommand(new Command("OK", 4, 1));
            var22.a_Form.addCommand(new Command("Trở về", 2, 1));
            var22.a_Form.setCommandListener(var22);
            da.a_da.a(var22.a_Form);
            return;
          }

          if (as.e_AString[as.a_B].equals("Cấy")) {
            if (o.e_AS == null) {
              o.d();
              o.e();
            }

            da.a_da.a_cd.a_as.a_AAB = o.a_AAB();
            if (da.a_da.a_cd.a_as.a_AAB == null) {
              da.a_da.a_cd.a_as.a("Tạm không có nguyên liệu để cấy", 8);
              return;
            }

            boolean var12 = true;

            for (int var21 = 0; var21 < 2; var21++) {
              if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var21][0] == 0) {
                var12 = false;
              }
            }

            if (var12) {
              da.a_da.a_cd.a_as.a("Số lần cấy sủng vật đã đạt giới hạn", 8);
              return;
            }

            da.a_da.a_cd.a_as.a((byte)3, (byte)7);
            return;
          }

          if (as.e_AString[as.a_B].equals("Tinh luyện")) {
            as.q_B = 8;
            return;
          }
        }
      } else if (as.q_B == 2) {
        if (var0 == b.a_S || var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)3, (byte)1);
          return;
        }

        if (var0 == 5) {
          if (da.a_da.a_cd.a_as.v_I < da.a_da.a_cd.a_as.u_I - 1) {
            da.a_da.a_cd.a_as.v_I++;
            return;
          }
        } else if (var0 == 2 && da.a_da.a_cd.a_as.v_I > 0) {
          da.a_da.a_cd.a_as.v_I--;
          return;
        }
      } else if (as.q_B == 3) {
        if (var0 == 8 || var0 == b.a_S) {
          o.d_V(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].b_I);
          o.e_V(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].b_I);
          as.a_Acv = null;
          if ((as.a_Acv = k.a_Acv()) == null) {
            da.a_da.a_cd.a_as.a("Không có sủng vật", 10);
            as.o_B = 1;
            return;
          }

          System.out.println("Thả");
          da.a_da.a_cd.a_as.a("Đã thả thành công!", 10);
          da.a_da.a_cd.a_as.a((byte)3, (byte)0);
          return;
        }

        if (var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)3, (byte)1);
          return;
        }
      } else if (as.q_B == 8) {
        if (var0 != 8 && var0 != b.a_S) {
          if (var0 == b.b_S) {
            da.a_da.a_cd.a_as.a((byte)3, (byte)1);
            return;
          }
        } else {
          if (!as.a_AAString[as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_I - 1][18].equals("-1")) {
            as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].b_AB = cg.b_AB(as.a_AAString[as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_I - 1][18], ",");
          }

          byte var10;
          o.b(var10 = as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].b_AB[0], 1);
          o.d_V(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].b_I);
          o.e_V(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].b_I);
          as.a_AAAS = null;
          da.a_da.a_cd.a_as.c();
          as.a_Acv = null;
          if ((as.a_Acv = k.a_Acv()) != null) {
            da.a_da.a_cd.a_as.a("Nhận vật phẩm " + o.f_AString[o.d_I(var10)], 10);
            da.a_da.a_cd.a_as.a((byte)3, (byte)0);
            da.a_da.a_cd.a_as.a();
            Image var20 = da.a_da.a_cd.a_as.a_Image;
            l var11 = da.a_da.a_cd.a_as.a_l;
            da.a_da.a_cd.a_as.a_l.a_Image = var20;
            return;
          }

          da.a_da.a_cd.a_as.a("Nhận vật phẩm " + o.f_AString[o.d_I(var10)], 10);
          as.o_B = 1;
          da.a_da.a_cd.a_as.a((byte)1, (byte)0);
          da.a_da.a_cd.a_as.a();
        }
      } else if (as.q_B == 4) {
        if (var0 == b.a_S || var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)3, (byte)0);
          return;
        }
      } else if (as.q_B == 5) {
        if (as.s_B == 0) {
          if (var0 == b.b_S) {
            as.q_B = 0;
            if (da.a_da.a_cd.a_as.a_l != null) {
              Image var19 = da.a_da.a_cd.a_as.a_Image;
              l var9 = da.a_da.a_cd.a_as.a_l;
              da.a_da.a_cd.a_as.a_l.a_Image = var19;
              return;
            }
          } else {
            if (var0 == b.a_S || var0 == 8) {
              as.s_B = 1;
              return;
            }

            if (var0 == 1) {
              da.a_da.a_cd.a_as.e_ab.b_V();
              return;
            }

            if (var0 == 6) {
              da.a_da.a_cd.a_as.e_ab.c_V();
              return;
            }
          }
        } else if (as.s_B == 1 && (var0 == 8 || var0 == b.a_S || var0 == b.b_S)) {
          as.s_B = 0;
          da.a_da.a_cd.a_as.a((byte)3, (byte)0);
          return;
        }
      } else if (as.q_B == 7) {
        if (da.a_da.a_cd.a_as.r_B == 0) {
          if (var0 == 8) {
            if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][0] != 0) {
              da.a_da.a_cd.a_as.a("Đã cấy ở đây ", 8);
              return;
            }

            da.a_da.a_cd.a_as.r_B = 1;
            return;
          }

          if (var0 == 2 || var0 == 5) {
            if (da.a_da.a_cd.a_as.t_I == 1) {
              da.a_da.a_cd.a_as.t_I = 0;
              return;
            }

            da.a_da.a_cd.a_as.t_I = 1;
            return;
          }

          if (var0 == b.a_S) {
            for (int var8 = 0; var8 < 2; var8++) {
              if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var8][0] != 1 && as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var8][1] != 0) {
                as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var8][0] = 1;
                int var18 = o.d_I(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var8][1]);
                o.a(as.a_Acv[da.a_da.a_cd.a_as.a_l.a()], o.h_AString[var18]);
                o.c(var18 + 1, 1);
                if (var8 == 0) {
                  da.a_da.a_cd.a_as.c_Z = true;
                } else {
                  da.a_da.a_cd.a_as.d_Z = true;
                }
              }
            }

            da.a_da.a_cd.a_as.a_AAB = null;
            da.a_da.a_cd.a_as.a_AAB = o.a_AAB();
            return;
          }

          if (var0 == b.b_S) {
            for (int var7 = 0; var7 < 2; var7++) {
              if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var7][0] == 0) {
                as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[var7][1] = 0;
              }
            }

            da.a_da.a_cd.a_as.a((byte)3, (byte)0);
            return;
          }
        } else if (da.a_da.a_cd.a_as.r_B == 1) {
          if (var0 != 8 && var0 != b.a_S) {
            if (var0 == b.b_S) {
              da.a_da.a_cd.a_as.r_B = 0;
              return;
            }

            if (var0 == 5) {
              if (da.a_da.a_cd.a_as.s_I < 17) {
                da.a_da.a_cd.a_as.s_I++;
              }

              if (da.a_da.a_cd.a_as.s_I - da.a_da.a_cd.a_as.r_I * as.f_S >= as.f_S * as.g_S) {
                da.a_da.a_cd.a_as.r_I++;
                return;
              }
            } else if (var0 == 2) {
              if (da.a_da.a_cd.a_as.r_I > 0 && da.a_da.a_cd.a_as.s_I == da.a_da.a_cd.a_as.r_I * as.f_S) {
                da.a_da.a_cd.a_as.r_I--;
              }

              if (da.a_da.a_cd.a_as.s_I > 0) {
                da.a_da.a_cd.a_as.s_I--;
                return;
              }
            } else if (var0 == 6) {
              if (da.a_da.a_cd.a_as.s_I + as.f_S <= 17) {
                da.a_da.a_cd.a_as.s_I = da.a_da.a_cd.a_as.s_I + as.f_S;
              } else if (da.a_da.a_cd.a_as.s_I < 17) {
                da.a_da.a_cd.a_as.s_I++;
              }

              if (da.a_da.a_cd.a_as.s_I - da.a_da.a_cd.a_as.r_I * as.f_S >= as.f_S * as.g_S) {
                da.a_da.a_cd.a_as.r_I++;
                return;
              }
            } else if (var0 == 1) {
              if (da.a_da.a_cd.a_as.s_I - as.f_S >= 0) {
                da.a_da.a_cd.a_as.s_I = da.a_da.a_cd.a_as.s_I - as.f_S;
              } else if (da.a_da.a_cd.a_as.s_I > 0) {
                da.a_da.a_cd.a_as.s_I--;
              }

              if (da.a_da.a_cd.a_as.r_I > 0 && da.a_da.a_cd.a_as.s_I < da.a_da.a_cd.a_as.r_I * as.f_S) {
                da.a_da.a_cd.a_as.r_I--;
                return;
              }
            }
          } else {
            if (da.a_da.a_cd.a_as.a_AAB == null || da.a_da.a_cd.a_as.s_I >= da.a_da.a_cd.a_as.a_AAB.length) {
              if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1] != 0) {
                var0 = -1;
                if (da.a_da.a_cd.a_as.a_AAB != null) {
                  for (int var16 = 0; var16 < da.a_da.a_cd.a_as.a_AAB.length; var16++) {
                    if (da.a_da.a_cd.a_as.a_AAB[var16][0] == as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1]) {
                      var0 = var16;
                      break;
                    }
                  }
                }

                if (var0 != -1) {
                  da.a_da.a_cd.a_as.a_AAB[var0][1]++;
                } else {
                  byte[] var17 = cg.a_AB(cg.a_AB(null, as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1]), 1);
                  da.a_da.a_cd.a_as.a_AAB = cg.a(da.a_da.a_cd.a_as.a_AAB, var17);
                }
              }

              as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1] = 0;
              return;
            }

            if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1] == 0) {
              as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1] = da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][0];
              if (da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][1] > 1) {
                da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][1]--;
                return;
              }

              da.a_da.a_cd.a_as.a_AAB = cg.a(da.a_da.a_cd.a_as.a_AAB, da.a_da.a_cd.a_as.s_I);
              return;
            }

            if (as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1] != da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][0]) {
              var0 = -1;

              for (int var1 = 0; var1 < da.a_da.a_cd.a_as.a_AAB.length; var1++) {
                if (da.a_da.a_cd.a_as.a_AAB[var1][0] == as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1]) {
                  var0 = var1;
                  break;
                }
              }

              if (var0 != -1) {
                da.a_da.a_cd.a_as.a_AAB[var0][1]++;
              } else {
                byte[] var15 = cg.a_AB(cg.a_AB(null, as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1]), 1);
                as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1] = da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][1];
                da.a_da.a_cd.a_as.a_AAB = cg.a(da.a_da.a_cd.a_as.a_AAB, var15);
              }

              as.a_Acv[da.a_da.a_cd.a_as.a_l.a()].a_AAB[da.a_da.a_cd.a_as.t_I][1] = da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][0];
              if (da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][1] <= 1) {
                da.a_da.a_cd.a_as.a_AAB = cg.a(da.a_da.a_cd.a_as.a_AAB, da.a_da.a_cd.a_as.s_I);
                return;
              }

              da.a_da.a_cd.a_as.a_AAB[da.a_da.a_cd.a_as.s_I][1]--;
            }
          }
        } else if (da.a_da.a_cd.a_as.r_B == 2 && (var0 == 8 || var0 == b.a_S || var0 == b.b_S)) {
          da.a_da.a_cd.a_as.k();
          da.a_da.a_cd.a_as.a((byte)3, (byte)1);
        }
      }
    }
  }

  private static void e() {
    as.l_B = 0;
    String[] var0 = RecordStore.listRecordStores();

    for (int var1 = 0; var0 != null && var1 < var0.length; var1++) {
      if (var0[var1].equals(o.m_String)) {
        o.c_Z = true;
        o.a_Z();
      }
    }

    if (o.c_Z) {
      as.a_Abx = new bx[o.j_AS.length];

      for (int var2 = 0; var2 < as.a_Abx.length; var2++) {
        as.a_Abx[var2] = new bx();
        as.a_Abx[var2].a("/av/chr" + o.j_AS[var2] + ".av");
      }
    }
  }

  private static void h(int var0) {
    if (as.p_B == 6) {
      if (var0 == b.b_S) {
        da.a_da.a_cd.a_as.a((byte)9, (byte)0);
        return;
      }

      if (var0 == 8 || var0 == b.a_S) {
        as.p_B = 0;
        bk.a(Main.self, da.a_da.a_Display, da.a_da, da.a_da.b_S, da.a_da.c_S, false, cg.a_I(cq.e_String), 120);
        bk.a_V(o.c_I);
        da.a_da.a_cd.a_as.c_ab.a(da.a_da.a_cd.a_as.a_Image, (at.a_AS[0] - 120) / 2);
        return;
      }
    } else if (as.p_B == 0) {
      if (var0 == b.b_S) {
        da.a_da.a_cd.a_as.a((byte)1, (byte)0);
        da.a_da.a_cd.a_as.f();
        return;
      }

      if (var0 != 8 && var0 != b.a_S) {
        if (var0 == 1) {
          da.a_da.a_cd.a_as.c_ab.b_V();
          return;
        }

        if (var0 == 6) {
          da.a_da.a_cd.a_as.c_ab.c_V();
          return;
        }
      } else {
        var0 = da.a_da.a_cd.a_as.c_ab.b_I();
        if (as.a_AString[var0].equals("Dịch vụ ưu đãi")) {
          da.a_da.a_cd.a_as.e();
          if (!at.a_String.equals("N73")
            && !at.a_String.equals("N7610")
            && !at.a_String.equals("N5800")
            && !at.a_String.equals("E62")
            && !at.a_String.equals("W958C")
            && da.a_da.a_cd.a_as != null) {
            da.a_da.a_cd.a_as.h();
          }

          da.a_da.a_cd.a_as = null;
          as.a_bl = null;
          k.c();
          df.a("/bin/smsmenu.bin");
        } else if (as.a_AString[var0].equals("Lưu game")) {
          da.a_da.a_cd.a_as.a((byte)9, (byte)1);
          e();
        } else if (as.a_AString[var0].equals("Tải game")) {
          da.a_da.a_cd.a_as.a((byte)9, (byte)2);
          e();
        } else if (as.a_AString[var0].equals("Cài đặt nhạc")) {
          da.a_da.a_cd.a_as.a((byte)9, (byte)3);
        } else {
          if (!as.a_AString[var0].equals("Thoát game")) {
            if (as.a_AString[var0].equals("Tải điểm QQ")) {
              da.a_da.a_cd.a_as.f();
              as.p_B = 6;
            }

            return;
          }

          as.p_B = 4;
        }
      }
    } else if (as.p_B == 1) {
      if (var0 == 8 || var0 == b.a_S) {
        if (o.b_S != 67) {
          if (o.c_AB[as.l_B] == 1) {
            as.D_B = 1;
            as.a_String = "Xác nhận ghi đè bản lưu hiện tại?";
            as.p_B = 5;
            return;
          }

          o.c_Z = true;
          o.a_Z(as.l_B);
          o.b(as.l_B);
          if (o.c_Z) {
            as.a_Abx = new bx[o.j_AS.length];

            for (int var3 = 0; var3 < as.a_Abx.length; var3++) {
              as.a_Abx[var3] = new bx();
              as.a_Abx[var3].a("/av/chr" + o.j_AS[var3] + ".av");
            }
          }

          as.p_B = 1;
          return;
        }

        da.a_da.a_cd.a_as.a("Không thể lưu ở cảnh này", 10);
        return;
      }

      if (var0 == b.b_S) {
        da.a_da.a_cd.a_as.a((byte)9, (byte)0);
        return;
      }

      if (var0 == 6) {
        if (as.l_B < at.e_B - 1) {
          as.l_B++;
          return;
        }
      } else if (var0 == 1 && as.l_B > 0) {
        as.l_B--;
        return;
      }
    } else if (as.p_B == 2) {
      if (var0 != 8 && var0 != b.a_S) {
        if (var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)9, (byte)0);
          return;
        }

        if (var0 == 6) {
          if (as.l_B < at.e_B - 1) {
            as.l_B++;
            return;
          }
        } else if (var0 == 1 && as.l_B > 0) {
          as.l_B--;
          return;
        }
      } else if (o.c_AB[as.l_B] == 1) {
        as.D_B = 2;
        as.p_B = 5;
        as.a_String = "Xác nhận tải bản lưu hiện tại?";
        return;
      }
    } else if (as.p_B == 4) {
      if (var0 != 8 && var0 != b.a_S) {
        if (var0 == b.b_S) {
          da.a_da.a_cd.a_as.a((byte)9, (byte)0);
          return;
        }
      } else {
        da.a_da.a_cd.b_B = 0;
        da.a_da.a_cd.a_as = null;

        try {
          Thread.sleep(300L);
        } catch (InterruptedException var1) {
          var1.printStackTrace();
        }

        if (da.a_da.a_cd != null) {
          cd.j();
        }

        Main.self.showCover();
        o.f();
        if (at.b_Z) {
          Main.self.playMusic(ct.a_String, -1, o.g_B);
          return;
        }
      }
    } else if (as.p_B == 3) {
      if (var0 == 8 || var0 == b.a_S || var0 == b.b_S) {
        da.a_da.a_cd.a_as.a((byte)9, (byte)0);
      }

      if (var0 == 5) {
        if (!at.b_Z) {
          at.b_Z = true;
        }

        if (o.g_B < 100) {
          o.g_B = (byte)(o.g_B + 10);
        }

        if (o.g_B >= 100) {
          o.g_B = 100;
        }

        if (da.a_da.a_cd.a_as.b_bx != null) {
          da.a_da.a_cd.a_as.b_bx.a_V(1);
        }

        if (Main.music != null) {
          Main.music.b(o.g_B);
        } else {
          Main.self.playMusic(ct.a_String, -1, o.g_B);
        }
      }

      if (var0 == 2) {
        if (o.g_B > 0) {
          o.g_B = (byte)(o.g_B - 10);
        }

        if (o.g_B <= 0) {
          o.g_B = 0;
          if (da.a_da.a_cd.a_as.b_bx != null) {
            da.a_da.a_cd.a_as.b_bx.a_V(0);
          }
        }

        Main.music.b(o.g_B);
        return;
      }
    } else if (as.p_B == 5) {
      if (var0 != 8 && var0 != b.a_S) {
        if (var0 == b.b_S) {
          as.p_B = as.D_B;
          as.D_B = 0;
        }
      } else {
        if (as.D_B != 2) {
          c_Z();
          as.D_B = 0;
          as.p_B = 1;
          da.a_da.a_cd.a_as.a("Lưu thành công", 8);
          return;
        }

        as.D_B = 0;
        if (o.c_AB[as.l_B] == 1) {
          String[] var2 = new String[]{"Tiến độ 1", "Tiến độ 2"};
          da.a_da.a_cd.b_B = 0;
          a_Z();
          da.a_da.a_cd.a_as = null;
          da.a_da.a_cd.a_String = null;
          o.f();
          da.a_da.a_cd.a(var2[as.l_B]);
          return;
        }
      }
    }
  }

  private static boolean c_Z() {
    o.c_Z = true;
    o.a_Z(as.l_B);
    o.b(as.l_B);
    if (o.c_Z) {
      as.a_Abx = new bx[o.j_AS.length];

      for (int var0 = 0; var0 < as.a_Abx.length; var0++) {
        as.a_Abx[var0] = new bx();
        as.a_Abx[var0].a("/av/chr" + o.j_AS[var0] + ".av");
      }
    }

    return true;
  }

  public static boolean a_Z() {
    da.a_da.a_cd.a_as.e();
    if (!at.a_String.equals("N73")
      && !at.a_String.equals("N7610")
      && !at.a_String.equals("N5800")
      && !at.a_String.equals("E62")
      && !at.a_String.equals("W958C")
      && da.a_da.a_cd.a_as != null) {
      da.a_da.a_cd.a_as.h();
    }

    da.a_da.a_cd.a_as = null;
    as.a_bl = null;
    k.c();
    if (at.f_Z) {
      da.a_da.a_cd.d();
    }

    try {
      Thread.sleep(30L);
    } catch (InterruptedException var1) {
      var1.printStackTrace();
    }

    da.a_da.a_cd.b_B = 1;
    return true;
  }

  public static boolean a(int var0, int var1, int var2, int var3, int var4, int var5) {
    boolean var6 = false;
    if (var2 < var0 && var0 < var2 + 30 && var3 < var1 && var1 < var3 + 30) {
      var6 = true;
    }

    return var6;
  }

  public static void a_V(int var0, int var1) {
    int var2 = da.a_da.a_cd.a_as.a_AI[0];
    int var3 = da.a_da.a_cd.a_as.a_AI[1] + da.a_da.a_cd.a_as.a_AI[3] + da.a_da.a_cd.a_as.j_I;
    int var4 = da.a_da.a_cd.a_as.a_AI[2];
    if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
      if (a(var0, var1, var2, var3, 30, 30)) {
        a_V(b.a_S);
      } else if (a(var0, var1, var2 + var4 - 30, var3, 30, 30)) {
        a_V(b.b_S);
      }
    } else if (a(var0, var1, var2, var3, 30, 30)) {
      a_V(b.b_S);
    } else if (a(var0, var1, var2 + var4 - 30, var3, 30, 30)) {
      a_V(b.a_S);
    }

    if (as.o_B == 8 && as.z_B == 0) {
      if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
        if (a(var0, var1, var2, var3 - da.a_da.a_cd.a_as.h_I - cg.b_S, 30, 30)) {
          da.a_da.a_cd.a_as.b_I--;
          da.a_da.a_cd.a_as.b_I = (da.a_da.a_cd.a_as.b_I + 3) % 3;
          return;
        }

        if (a(var0, var1, var2 + var4 - 30, var3 - da.a_da.a_cd.a_as.h_I - cg.b_S, 30, 30)) {
          da.a_da.a_cd.a_as.b_I++;
          da.a_da.a_cd.a_as.b_I %= 3;
        }
      } else {
        if (a(var0, var1, var2, var3 - da.a_da.a_cd.a_as.h_I - cg.b_S, 30, 30)) {
          da.a_da.a_cd.a_as.b_I++;
          da.a_da.a_cd.a_as.b_I %= 3;
          return;
        }

        if (a(var0, var1, var2 + var4 - 30, var3 - da.a_da.a_cd.a_as.h_I - cg.b_S, 30, 30)) {
          da.a_da.a_cd.a_as.b_I--;
          da.a_da.a_cd.a_as.b_I = (da.a_da.a_cd.a_as.b_I + 3) % 3;
          return;
        }
      }
    }
  }

  public ab(int var1, int var2, int var3, int var4) {
    this.f_I = 0;
    this.a_I = var1;
    this.b_I = var2;
    this.c_I = var3;
    this.d_I = var4;
    this.a_Vector = new Vector();
  }

  public void a(Image var1, int var2) {
    this.a_Image = var1;
    this.e_I = var2;
  }

  public int a_I(int var1) {
    return this.a_AI[var1];
  }

  public int a_I() {
    return this.a_Vector != null ? this.a_Vector.size() : 0;
  }

  public int b_I() {
    return ((bg)this.a_Vector.elementAt(0)).c();
  }

  public void a(bg var1) {
    this.a_Vector.addElement(var1);
  }

  public void a_V() {
    for (int var1 = 0; var1 < this.a_Vector.size(); var1++) {
      ((bg)this.a_Vector.elementAt(var1)).a_V();
    }

    this.a_Vector.removeAllElements();
  }

  public void b_V() {
    for (int var1 = 0; var1 < this.a_Vector.size(); var1++) {
      ((bg)this.a_Vector.elementAt(var1)).f();
    }
  }

  public void c_V() {
    for (int var1 = 0; var1 < this.a_Vector.size(); var1++) {
      ((bg)this.a_Vector.elementAt(var1)).e();
    }
  }

  public void a(Graphics var1) {
    if (da.a_da.a_S % 12 == 0) {
      this.f_I = 3;
    } else {
      this.f_I = 0;
    }

    if (this.a_Vector != null && this.a_Vector.size() > 0) {
      if (((bg)this.a_Vector.elementAt(0)).a_Vector.size() > ((bg)this.a_Vector.elementAt(0)).i_I) {
        k.a(
          var1,
          null,
          null,
          this.a_I + this.c_I - 15,
          this.b_I + 5,
          this.d_I - 10,
          ((bg)this.a_Vector.elementAt(0)).a_Vector.size(),
          ((bg)this.a_Vector.elementAt(0)).i_I,
          ((bg)this.a_Vector.elementAt(0)).h_I
        );
      }

      for (int var2 = 0; var2 < this.a_Vector.size(); var2++) {
        ((bg)this.a_Vector.elementAt(var2)).a(var1);
      }

      int var5 = this.b_I + (((bg)this.a_Vector.elementAt(0)).j_I >> 1);
      int var3;
      if ((var3 = ((bg)this.a_Vector.elementAt(0)).h_I + ((bg)this.a_Vector.elementAt(0)).i_I) > ((bg)this.a_Vector.elementAt(0)).a_Vector.size()) {
        var3 = ((bg)this.a_Vector.elementAt(0)).a_Vector.size();
      }

      for (int var4 = ((bg)this.a_Vector.elementAt(0)).h_I; var4 < var3; var4++) {
        if (var4 == this.b_I() && this.a_Image != null) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, this.e_I + 8 + this.f_I, var5, 3);
        }

        var5 += ((bg)this.a_Vector.elementAt(0)).j_I;
      }
    }
  }

  void b(int var1) {
    for (int var2 = 0; var2 < this.a_Vector.size(); var2++) {
      ((bg)this.a_Vector.elementAt(var2)).a(0);
    }
  }

  void d() {
    for (int var1 = 0; var1 < this.a_Vector.size(); var1++) {
      ((bg)this.a_Vector.elementAt(var1)).b(0);
    }
  }
}
