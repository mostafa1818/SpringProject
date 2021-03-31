

const dictionary = {
    "About": {
        'fr': 'A propos',
        'es': 'nos'
    },
    "Hello": {
        'fr': 'Bonjour',
        'es': 'Hola',
    }
};



const translator = new Translator(dictionary);
translator.translate('fr');
  translator.translate('es');