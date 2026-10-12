final class LotteryRequest extends PayServerRequest {
   public int amount;
   private SmsPaymentFlow flow;

   LotteryRequest(SmsPaymentFlow var1) {
      this.flow = var1;
   }

   public final void onPayResponse(int var1, int var2, String var3) {
      if (var1 == 12) {
         if (var2 == 0) {
            SmsPaymentFlow.setGateInfo(var3);
            SmsPaymentFlow.loadGateOf(this.flow, this.amount);
            SmsPaymentFlow.setSentCountOf(this.flow, (byte)SmsPaymentFlow.sentCountOf(this.flow, SmsPaymentFlow.productIdOf(this.flow)));
            SmsPaymentFlow.saveGateInfo(this.flow);
            SmsPaymentFlow.setLotteryWon(this.flow, Utils.getBool(SmsPaymentFlow.getGateInfo(), "lotteryResult"));
            SmsPaymentFlow.setLotteryMsg(this.flow, Utils.getValue(SmsPaymentFlow.getGateInfo(), "lotteryMsg"));
            if (SmsPaymentFlow.isLotteryWon(this.flow)) {
               SmsPaymentFlow.setNeedBuyAmount(this.flow, Utils.getInt(SmsPaymentFlow.getGateInfo(), "needBuyAmount"));
               SmsPaymentFlow.setHandselAmount(this.flow, Utils.getInt(SmsPaymentFlow.getGateInfo(), "handselAmount"));
               SmsPaymentFlow.resetBoughtCount(this.flow, 0);
               SmsPaymentFlow.resetHandselCount(this.flow, 0);
               SmsPaymentFlow.saveLotteryDate(this.flow);
               SmsPaymentFlow.saveHandselRecordOf(this.flow);
            }

            SmsPaymentFlow.setState((byte)2);
            return;
         }

         boolean var10001 = true;
         SmsPaymentFlow.setState((byte)5);
      }
   }
}
