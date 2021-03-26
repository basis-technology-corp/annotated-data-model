@Grab("com.basistech:common-lib:38.0.2")
import com.basistech.util.LanguageCode
import groovy.json.JsonOutput

def langcodes = LanguageCode.enumConstants.collect { it.ISO639_3() }

// don't bother
println JsonOutput.toJson(langcodes)
