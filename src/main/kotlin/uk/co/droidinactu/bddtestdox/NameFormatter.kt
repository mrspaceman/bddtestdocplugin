package uk.co.droidinactu.bddtestdox

import java.util.*

/**
 * reformats the name of a test method to be more readable.
 *
 *
 * [Adapted from Chris Stevenson's AgileDox
 * project.](http://agiledox.sourceforge.net/)
 */
class NameFormatter {
    private var suffix: String? = "Test"
    private var prefix: String? = "Test"
    fun prettifyTestClass(className: String): String {
        var title = className
        if (suffix != null && title.endsWith(suffix!!)) {
            title = title.take(title.lastIndexOf(suffix!!))
        }
        if (prefix != null && title.startsWith(prefix!!)) {
            title = title.substring(prefix!!.length)
        }
        return title
    }

    fun setTestSuffix(suffix: String?) {
        this.suffix = suffix
    }

    fun setSuffix(suffix: String?) {
        this.suffix = suffix
    }

    fun setPrefix(prefix: String?) {
        this.prefix = prefix
    }

    fun prettifyTestMethod(testName: String): String {
        var tn2 = testName
        if (testName.startsWith(TEST_PREFIX)) {
            tn2 = testName.trim { it <= ' ' }.substring(TEST_PREFIX.length)
        } else if (testName.startsWith(TEST_PREFIX_ALTERNATIVE)) {
            tn2 = testName.trim { it <= ' ' }.substring(TEST_PREFIX_ALTERNATIVE.length)
        }
        val words =
            tn2.replace("test", "").split("(?<!^)(?=[A-Z])".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val ftn = StringBuilder()
        for (wrd in words) {
            ftn.append(wrd.lowercase(Locale.getDefault())).append(" ")
        }
        return ftn.toString().trim { it <= ' ' }
    }

    fun prettifyScenarioName(scenarioName: String): String {
        return scenarioName.trim { it <= ' ' }.substring("Scenario:".length).trim { it <= ' ' }
    }

    fun prettifyFeatureName(featureName: String): String {
        return featureName // .trim().substring("Feature:".length()).trim();
    }

    companion object {
        /** test method name prefix  */
        const val TEST_PREFIX = "test"

        /** alternative test method name prefix  */
        const val TEST_PREFIX_ALTERNATIVE = "should"
    }
}