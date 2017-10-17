package com.surat.actions;

import com.surat.common.Initialization;

public class AllActions extends BaseActions {

    public static boolean GoToPercentCalculator() {
        if (stopOnFailure && stepFailed) return false;
        try {
            return Initialization.firstPage.GoToPercentCalculator();
        } catch (Exception e) {
            return false;
        }
    }


    public static String GetResult(String inputA, String inputB) {
        if (stopOnFailure && stepFailed) return null;
        try {
            Initialization.secondPage.uf_EnterInput(inputA, inputB);
            String result = Initialization.secondPage.resultText.getText();
            Initialization.secondPage.uf_ClearText();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
