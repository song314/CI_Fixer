package javaTest;

import com.pinguo.common.ciutils.fix.CheckStyleFixer;
import sun.awt.image.ImageWatched;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by tangsong on 12/16/14.
 */
public class TestMain {

    public static void main(String[] arg) {
        String str = "     private final static byte EQUALS_SIGN = (byte) '=';";

        String[] wordArray = str.split(" ");

        HashSet<String> modifierSet = new HashSet<String>();

        StringBuilder oldModifier = new StringBuilder();
        for (String w : wordArray) {
            if (CheckStyleFixer.MODIFIERS_SET.contains(w)) {
                modifierSet.add(w);
                oldModifier.append(w).append(" ");
            }
        }

        StringBuilder newModifier = new StringBuilder();
        for (String modifier : CheckStyleFixer.MODIFIERS) {
            if (modifierSet.contains(modifier)) {
                newModifier.append(modifier).append(" ");
            }
        }

        str = str.replace(oldModifier.toString(), newModifier.toString());
    }
}
