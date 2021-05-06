import json


# load language codes (languages.groovy)
with open("language_codes.json") as f:
    language_codes = json.load(f)

with open("adm-schema-generated.json") as f:
    schema = json.load(f)

    transformed = {
        "$schema": "http://json-schema.org/draft-04/schema#",
        "title": "Annotated Text",
        "type": "object",
        "additionalProperties": False
    }

    props = {}

    # data and documentMetadata are OK
    props["data"] = schema["properties"]["data"]
    props["documentMetadata"] = schema["properties"]["documentMetadata"]

    # properties of "attributes"
    ap = {}
    for k, v in schema["properties"].items():
        # ADM nodes are inside attributes; fix attributes later
        if k not in ["data", "documentMetadata", "attributes"]:
            # some attributes are singular names... please use field names..
            if k in ["tokens", "sentences", "layoutRegions", "scriptRegions"]:
                k = k[0:-1]

            if k == "concepts":
                itemType = "concept"
            elif k == "events":
                itemType = "event"
            elif k == "keyphrases":
                itemType = "keyphrase"
            elif k == "sentimentResults":
                itemType = "categorizerResults"
            else:
                itemType = k

            # ListAttribute may have any k/v as metadata ("token" uses it)
            ap[k] = {"type": "object", "additionalProperties": True,
                "properties": {
                    "type": { "type": "string", "enum": ["list"] },
                    "itemType": { "type": "string", "enum": [itemType]},
                    "items": v}}

    # "languageDetection" doesn't return ListAttribute, but just plain attribute.
    ap["languageDetection"] = { "$ref": "#/definitions/LanguageDetection" }

    # languageDetectionRegions returns ListAttribute of languageDetection
    ap["languageDetectionRegions"]["properties"]["itemType"]["enum"] = ["languageDetection"]

    ap["transliteration"] = {"type": "object", "additionalProperties": True,
                "properties": {
                    "type": { "type": "string", "enum": ["transliteration"] },
                    "results": { "$ref": "#/definitions/Transliteration"}}}

    # fix-up "attributes"
    props["attributes"] = {"type": "object", "additionalProperties": False,
        "properties": dict(sorted(ap.items())) }

    # version is missing
    props["version"] = {"type": "string"}

    transformed["properties"] = props

    # definitions are all good except language codes
    definitions = schema["definitions"]

    definitions["LanguageCode"] = {
        "type": "string",
        "enum": language_codes
    }

    # languages are in DetectionResult and TextDomain
    langref = {"$ref": "#/definitions/LanguageCode"}
    definitions["DetectionResult"]["properties"]["language"] = langref
    definitions["TextDomain"]["properties"]["language"] = langref

    # script
    definitions["ISO15924"] = {
        "type": "string",
        "enum": definitions["DetectionResult"]["properties"]["script"]["enum"]
    }

    scriptref = {"$ref": "#/definitions/ISO15924"}
    definitions["DetectionResult"]["properties"]["script"] = scriptref
    definitions["TextDomain"]["properties"]["script"] = scriptref
    definitions["ScriptRegion"]["properties"]["script"] = scriptref

    # LanguageDetection is missing "type"
    definitions["LanguageDetection"]["properties"]["type"] = {"type": "string"}

    transformed["definitions"] = dict(sorted(definitions.items()))

    with open("adm-schema.json", mode="w") as f1:
        json.dump(transformed, f1, indent=2)
    


    


    
