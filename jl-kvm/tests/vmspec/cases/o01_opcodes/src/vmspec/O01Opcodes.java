package vmspec;

import javax.microedition.midlet.MIDlet;

/** Opcode coverage not reached by the other cases: float/double/long locals and arithmetic, array stores, stack ops, switches, ldc_w. */
public class O01Opcodes extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static float fl(float a, float b, float c) {
        float d = a - b;
        float e = d * c;
        float g = e + 2f;
        float h = g / 4f;
        return -h % 3f + 1f;
    }

    static double db(double a, double b, double c, double d, double e) {
        double x = a + b;
        double y = x * c - d;
        double z = y / e;
        return -z + 0.5d + 1d;
    }

    static long lg(long a, long b) {
        long c = a + b;
        long d = c * a - b;
        long e = (d & 0xFF0FL) | (a ^ b);
        return e ^ (c << 3) ^ (d >> 2) ^ (a >>> 5) ^ -b;
    }

    static int sw1(int k) {
        switch (k) {
            case 1: return 10;
            case 2: return 20;
            case 3: return 30;
            case 5: return 50;
            default: return -1;
        }
    }

    static int sw2(int k) {
        switch (k) {
            case -1000: return 1;
            case 7: return 2;
            case 100000: return 3;
            default: return 0;
        }
    }

    static float fret(int i) { return i * 0.5f; }
    static double dret(int i) { return i * 0.25d; }
    static long lret(int i) { return (long) i << 33; }

    static int manyConstants() {
        int sum = 0;
        sum += "s00".length() + 100000;
        sum += "s11".length() + 100001;
        sum += "s22".length() + 100002;
        sum += "s33".length() + 100003;
        sum += "s44".length() + 100004;
        sum += "s55".length() + 100005;
        sum += "s66".length() + 100006;
        sum += "s77".length() + 100007;
        sum += "s88".length() + 100008;
        sum += "s99".length() + 100009;
        sum += "s1010".length() + 100010;
        sum += "s1111".length() + 100011;
        sum += "s1212".length() + 100012;
        sum += "s1313".length() + 100013;
        sum += "s1414".length() + 100014;
        sum += "s1515".length() + 100015;
        sum += "s1616".length() + 100016;
        sum += "s1717".length() + 100017;
        sum += "s1818".length() + 100018;
        sum += "s1919".length() + 100019;
        sum += "s2020".length() + 100020;
        sum += "s2121".length() + 100021;
        sum += "s2222".length() + 100022;
        sum += "s2323".length() + 100023;
        sum += "s2424".length() + 100024;
        sum += "s2525".length() + 100025;
        sum += "s2626".length() + 100026;
        sum += "s2727".length() + 100027;
        sum += "s2828".length() + 100028;
        sum += "s2929".length() + 100029;
        sum += "s3030".length() + 100030;
        sum += "s3131".length() + 100031;
        sum += "s3232".length() + 100032;
        sum += "s3333".length() + 100033;
        sum += "s3434".length() + 100034;
        sum += "s3535".length() + 100035;
        sum += "s3636".length() + 100036;
        sum += "s3737".length() + 100037;
        sum += "s3838".length() + 100038;
        sum += "s3939".length() + 100039;
        sum += "s4040".length() + 100040;
        sum += "s4141".length() + 100041;
        sum += "s4242".length() + 100042;
        sum += "s4343".length() + 100043;
        sum += "s4444".length() + 100044;
        sum += "s4545".length() + 100045;
        sum += "s4646".length() + 100046;
        sum += "s4747".length() + 100047;
        sum += "s4848".length() + 100048;
        sum += "s4949".length() + 100049;
        sum += "s5050".length() + 100050;
        sum += "s5151".length() + 100051;
        sum += "s5252".length() + 100052;
        sum += "s5353".length() + 100053;
        sum += "s5454".length() + 100054;
        sum += "s5555".length() + 100055;
        sum += "s5656".length() + 100056;
        sum += "s5757".length() + 100057;
        sum += "s5858".length() + 100058;
        sum += "s5959".length() + 100059;
        sum += "s6060".length() + 100060;
        sum += "s6161".length() + 100061;
        sum += "s6262".length() + 100062;
        sum += "s6363".length() + 100063;
        sum += "s6464".length() + 100064;
        sum += "s6565".length() + 100065;
        sum += "s6666".length() + 100066;
        sum += "s6767".length() + 100067;
        sum += "s6868".length() + 100068;
        sum += "s6969".length() + 100069;
        sum += "s7070".length() + 100070;
        sum += "s7171".length() + 100071;
        sum += "s7272".length() + 100072;
        sum += "s7373".length() + 100073;
        sum += "s7474".length() + 100074;
        sum += "s7575".length() + 100075;
        sum += "s7676".length() + 100076;
        sum += "s7777".length() + 100077;
        sum += "s7878".length() + 100078;
        sum += "s7979".length() + 100079;
        sum += "s8080".length() + 100080;
        sum += "s8181".length() + 100081;
        sum += "s8282".length() + 100082;
        sum += "s8383".length() + 100083;
        sum += "s8484".length() + 100084;
        sum += "s8585".length() + 100085;
        sum += "s8686".length() + 100086;
        sum += "s8787".length() + 100087;
        sum += "s8888".length() + 100088;
        sum += "s8989".length() + 100089;
        sum += "s9090".length() + 100090;
        sum += "s9191".length() + 100091;
        sum += "s9292".length() + 100092;
        sum += "s9393".length() + 100093;
        sum += "s9494".length() + 100094;
        sum += "s9595".length() + 100095;
        sum += "s9696".length() + 100096;
        sum += "s9797".length() + 100097;
        sum += "s9898".length() + 100098;
        sum += "s9999".length() + 100099;
        sum += "s100100".length() + 100100;
        sum += "s101101".length() + 100101;
        sum += "s102102".length() + 100102;
        sum += "s103103".length() + 100103;
        sum += "s104104".length() + 100104;
        sum += "s105105".length() + 100105;
        sum += "s106106".length() + 100106;
        sum += "s107107".length() + 100107;
        sum += "s108108".length() + 100108;
        sum += "s109109".length() + 100109;
        sum += "s110110".length() + 100110;
        sum += "s111111".length() + 100111;
        sum += "s112112".length() + 100112;
        sum += "s113113".length() + 100113;
        sum += "s114114".length() + 100114;
        sum += "s115115".length() + 100115;
        sum += "s116116".length() + 100116;
        sum += "s117117".length() + 100117;
        sum += "s118118".length() + 100118;
        sum += "s119119".length() + 100119;
        sum += "s120120".length() + 100120;
        sum += "s121121".length() + 100121;
        sum += "s122122".length() + 100122;
        sum += "s123123".length() + 100123;
        sum += "s124124".length() + 100124;
        sum += "s125125".length() + 100125;
        sum += "s126126".length() + 100126;
        sum += "s127127".length() + 100127;
        sum += "s128128".length() + 100128;
        sum += "s129129".length() + 100129;
        sum += "s130130".length() + 100130;
        sum += "s131131".length() + 100131;
        sum += "s132132".length() + 100132;
        sum += "s133133".length() + 100133;
        sum += "s134134".length() + 100134;
        sum += "s135135".length() + 100135;
        sum += "s136136".length() + 100136;
        sum += "s137137".length() + 100137;
        sum += "s138138".length() + 100138;
        sum += "s139139".length() + 100139;
        sum += "s140140".length() + 100140;
        sum += "s141141".length() + 100141;
        sum += "s142142".length() + 100142;
        sum += "s143143".length() + 100143;
        sum += "s144144".length() + 100144;
        sum += "s145145".length() + 100145;
        sum += "s146146".length() + 100146;
        sum += "s147147".length() + 100147;
        sum += "s148148".length() + 100148;
        sum += "s149149".length() + 100149;
        sum += "s150150".length() + 100150;
        sum += "s151151".length() + 100151;
        sum += "s152152".length() + 100152;
        sum += "s153153".length() + 100153;
        sum += "s154154".length() + 100154;
        sum += "s155155".length() + 100155;
        sum += "s156156".length() + 100156;
        sum += "s157157".length() + 100157;
        sum += "s158158".length() + 100158;
        sum += "s159159".length() + 100159;
        sum += "s160160".length() + 100160;
        sum += "s161161".length() + 100161;
        sum += "s162162".length() + 100162;
        sum += "s163163".length() + 100163;
        sum += "s164164".length() + 100164;
        sum += "s165165".length() + 100165;
        sum += "s166166".length() + 100166;
        sum += "s167167".length() + 100167;
        sum += "s168168".length() + 100168;
        sum += "s169169".length() + 100169;
        sum += "s170170".length() + 100170;
        sum += "s171171".length() + 100171;
        sum += "s172172".length() + 100172;
        sum += "s173173".length() + 100173;
        sum += "s174174".length() + 100174;
        sum += "s175175".length() + 100175;
        sum += "s176176".length() + 100176;
        sum += "s177177".length() + 100177;
        sum += "s178178".length() + 100178;
        sum += "s179179".length() + 100179;
        sum += "s180180".length() + 100180;
        sum += "s181181".length() + 100181;
        sum += "s182182".length() + 100182;
        sum += "s183183".length() + 100183;
        sum += "s184184".length() + 100184;
        sum += "s185185".length() + 100185;
        sum += "s186186".length() + 100186;
        sum += "s187187".length() + 100187;
        sum += "s188188".length() + 100188;
        sum += "s189189".length() + 100189;
        sum += "s190190".length() + 100190;
        sum += "s191191".length() + 100191;
        sum += "s192192".length() + 100192;
        sum += "s193193".length() + 100193;
        sum += "s194194".length() + 100194;
        sum += "s195195".length() + 100195;
        sum += "s196196".length() + 100196;
        sum += "s197197".length() + 100197;
        sum += "s198198".length() + 100198;
        sum += "s199199".length() + 100199;
        sum += "s200200".length() + 100200;
        sum += "s201201".length() + 100201;
        sum += "s202202".length() + 100202;
        sum += "s203203".length() + 100203;
        sum += "s204204".length() + 100204;
        sum += "s205205".length() + 100205;
        sum += "s206206".length() + 100206;
        sum += "s207207".length() + 100207;
        sum += "s208208".length() + 100208;
        sum += "s209209".length() + 100209;
        sum += "s210210".length() + 100210;
        sum += "s211211".length() + 100211;
        sum += "s212212".length() + 100212;
        sum += "s213213".length() + 100213;
        sum += "s214214".length() + 100214;
        sum += "s215215".length() + 100215;
        sum += "s216216".length() + 100216;
        sum += "s217217".length() + 100217;
        sum += "s218218".length() + 100218;
        sum += "s219219".length() + 100219;
        sum += "s220220".length() + 100220;
        sum += "s221221".length() + 100221;
        sum += "s222222".length() + 100222;
        sum += "s223223".length() + 100223;
        sum += "s224224".length() + 100224;
        sum += "s225225".length() + 100225;
        sum += "s226226".length() + 100226;
        sum += "s227227".length() + 100227;
        sum += "s228228".length() + 100228;
        sum += "s229229".length() + 100229;
        sum += "s230230".length() + 100230;
        sum += "s231231".length() + 100231;
        sum += "s232232".length() + 100232;
        sum += "s233233".length() + 100233;
        sum += "s234234".length() + 100234;
        sum += "s235235".length() + 100235;
        sum += "s236236".length() + 100236;
        sum += "s237237".length() + 100237;
        sum += "s238238".length() + 100238;
        sum += "s239239".length() + 100239;
        sum += "s240240".length() + 100240;
        sum += "s241241".length() + 100241;
        sum += "s242242".length() + 100242;
        sum += "s243243".length() + 100243;
        sum += "s244244".length() + 100244;
        sum += "s245245".length() + 100245;
        sum += "s246246".length() + 100246;
        sum += "s247247".length() + 100247;
        sum += "s248248".length() + 100248;
        sum += "s249249".length() + 100249;
        sum += "s250250".length() + 100250;
        sum += "s251251".length() + 100251;
        sum += "s252252".length() + 100252;
        sum += "s253253".length() + 100253;
        sum += "s254254".length() + 100254;
        sum += "s255255".length() + 100255;
        sum += "s256256".length() + 100256;
        sum += "s257257".length() + 100257;
        sum += "s258258".length() + 100258;
        sum += "s259259".length() + 100259;
        sum += "s260260".length() + 100260;
        sum += "s261261".length() + 100261;
        sum += "s262262".length() + 100262;
        sum += "s263263".length() + 100263;
        sum += "s264264".length() + 100264;
        sum += "s265265".length() + 100265;
        sum += "s266266".length() + 100266;
        sum += "s267267".length() + 100267;
        sum += "s268268".length() + 100268;
        sum += "s269269".length() + 100269;
        sum += "s270270".length() + 100270;
        sum += "s271271".length() + 100271;
        sum += "s272272".length() + 100272;
        sum += "s273273".length() + 100273;
        sum += "s274274".length() + 100274;
        sum += "s275275".length() + 100275;
        sum += "s276276".length() + 100276;
        sum += "s277277".length() + 100277;
        sum += "s278278".length() + 100278;
        sum += "s279279".length() + 100279;
        sum += "s280280".length() + 100280;
        sum += "s281281".length() + 100281;
        sum += "s282282".length() + 100282;
        sum += "s283283".length() + 100283;
        sum += "s284284".length() + 100284;
        sum += "s285285".length() + 100285;
        sum += "s286286".length() + 100286;
        sum += "s287287".length() + 100287;
        sum += "s288288".length() + 100288;
        sum += "s289289".length() + 100289;
        sum += "s290290".length() + 100290;
        sum += "s291291".length() + 100291;
        sum += "s292292".length() + 100292;
        sum += "s293293".length() + 100293;
        sum += "s294294".length() + 100294;
        sum += "s295295".length() + 100295;
        sum += "s296296".length() + 100296;
        sum += "s297297".length() + 100297;
        sum += "s298298".length() + 100298;
        sum += "s299299".length() + 100299;
        return sum;
    }

    protected void startApp() {
        p("fl " + fl(7.5f, 2.25f, 3f));
        p("db " + db(1.5, 2.5, 3.5, 4.5, 5.5));
        p("lg " + lg(0x123456789L, 0x0FEDCBAL));
        p("sw1 " + sw1(1) + " " + sw1(2) + " " + sw1(3) + " " + sw1(4) + " " + sw1(5) + " " + sw1(0));
        p("sw2 " + sw2(-1000) + " " + sw2(7) + " " + sw2(100000) + " " + sw2(8));
        p("rets " + fret(5) + " " + dret(5) + " " + lret(5));
        p("many " + manyConstants());
        float[] fa = new float[3];
        double[] da = new double[3];
        long[] la = new long[3];
        fa[1] = 1.5f; da[2] = 2.5d; la[0] = 1L << 40;
        fa[1] += 2f; da[2] *= 2d; la[0] += 5L; la[1]++; la[2]--;
        p("arrays " + fa[1] + " " + da[2] + " " + la[0] + " " + la[1] + " " + la[2] + " " + fa[0] + " " + da[0]);
        int[] ia = new int[2];
        int k = 0;
        ia[k++] += 5;
        ia[k] = ia[k - 1] * 3;
        int x = ia[0], y = ia[1];
        int z = (x = y) + (y = x);
        p("incs " + ia[0] + " " + ia[1] + " " + k + " " + z);
        long m = 5L, n = 7L;
        long q = m++ + n--;
        double dd = 3.0;
        float ff = 2.0f;
        dd += ff;
        ff -= (float) dd;
        p("conv " + q + " " + m + " " + n + " " + dd + " " + ff + " " + (float) m + " " + (double) n + " " + (double) ff + " " + (float) dd + " " + (long) dd + " " + (int) ff);
        long pl = 3L; double pd = 1.5;
        Object[] oa = new Object[2];
        oa[0] = "a";
        String s = (String) oa[0];
        p("misc " + s + " " + (pl * 2) + " " + (-pd) + " " + (pl & 6L) + " " + (pl | 8L) + " " + (pl ^ 1L) + " " + (pd - 0.25) + " " + (pd * 2));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
