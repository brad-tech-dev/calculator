package com.adekorir.swing.calculator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.logging.Level;
import java.util.logging.Logger;

class CalcProcessor {

    private ScriptEngine scriptEngine;

    CalcProcessor() {
        super();

        // default script engine is javascript
        scriptEngine = new ScriptEngineManager().getEngineByExtension("js");
    }

    String process(String evaluationString) throws ProcessException {
        Logger.getLogger(CalcProcessor.class.getName()).log(Level.INFO, "Calculating: " + evaluationString);
        try {
            Object retVal = scriptEngine.eval(evaluationString);
            return retVal.toString();
        } catch (ScriptException ex) {
            throw new ProcessException(ex);
        }
    }
}
