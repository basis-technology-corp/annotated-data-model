How to generate ADM JSON Schema
===============================

Steps to genereate `adm-schema.json`.

1. Run `mvn verify`. This generates `adm-schema-generated.json`.
2. Run `python transform.py`.
3. Verify `adm-schema.json` with tools like https://python-jsonschema.readthedocs.io/en/stable/.

Transformations
===============

* Keep all `definitions`. Replace language codes with `LanguageCode` enum names. 

* Turn the generated properties to objects and move `items` inside.

```
   "sentences": {
      "type": "array",
      "items": {
        "$ref": "#/definitions/Sentence"
      }
    },
```

Transformed:

```
       "sentence": {
          "type": "object",
          "properties": {
            "type": {
              "type": "string"
            },
            "itemType": {
              "type": "string"
            },
            "items": {
              "type": "array",
              "items": {
                "$ref": "#/definitions/Sentence"
              }
            }
          }
        }
```

Notes
=====

* Property names (that hold ADM types) are mostly plural but some are singular. 

* Generate LanguageCode values `language_codes.json` with `languages.groovy`.

* `LanguageDetection` doesn't use `ListAttribute` but straight object.
