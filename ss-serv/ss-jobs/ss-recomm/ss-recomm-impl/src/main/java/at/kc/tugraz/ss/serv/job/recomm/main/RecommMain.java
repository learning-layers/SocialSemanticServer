/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.kc.tugraz.ss.serv.job.recomm.main;

import at.kc.tugraz.ss.serv.job.recomm.impl.engine.BaseLevelLearningEngine;
import at.kc.tugraz.ss.serv.job.recomm.impl.engine.LanguageModelEngine;
import at.kc.tugraz.ss.serv.job.recomm.impl.engine.ThreeLayersEngine;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dkowald
 */
public class RecommMain {
    public static void main(String[] args) {
        try {
		ThreeLayersEngine engine = new ThreeLayersEngine();
		engine.loadFile("cul_core/cul_sample_1_res");
		System.out.println("3LT: " + engine.getTags("41", "545", Arrays.asList("ontology", "conference", "tutorial", "web2.0", "rss", "tools"), 10, true));
		System.out.println("3L: " + engine.getTags("41", "545", Arrays.asList("ontology", "conference", "tutorial", "web2.0", "rss", "tools"), 10, false));
		BaseLevelLearningEngine bllEngine = new BaseLevelLearningEngine();
		bllEngine.loadFile("cul_core/cul_sample_1_res");
		System.out.println("BLL: " + bllEngine.getTags("41", "545", 10));
                LanguageModelEngine lmEngine = new LanguageModelEngine();
                lmEngine.loadFile("cul_core/cul_sample_1_res");
                System.out.println("LM: " + lmEngine.getTags("41", "545", 10));
        } catch (Exception ex) {
            Logger.getLogger(RecommMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
