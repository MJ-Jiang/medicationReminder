import i18n from "i18next";
import { initReactI18next } from "react-i18next";

// the translations
// (tip move them in a JSON file and import them,
// or even better, manage them separated from your code: https://react.i18next.com/guides/multiple-translation-files)
const resources = {
  en: {
    translation: {
      "Welcome to React": "Welcome to React and react-i18next",
      "Search for medication":"Search for medication",
      "+ New Reminder":"+ New reminder",
      "Name":"Name",
      "Description":"Description",
      "Dosage":"Dosage",
      "Start Date":"Start Date",
      "Frequency":"Frequency",
      "End Date":"End Date",
      "Reminder Time":"Reminder Time",
      "Add Reminder":"Add Rminder",
      "Purpose":"Purpose",
      "Select Frequency":"Select Frequency",
      "Pill Reminders":"Pill Reminders",
      "New Pill Reminder":"New Pill Reminder"
      
      /**待补充 */
    }
  },
  fi: {
    translation: {
      "Welcome to React": "Bienvenue à React et react-i18next",
      "Search for medication":"Etsi lääkkeitä",
      "+ New Reminder":"+ Uusi Muistutus",
      "Name":"Nimi",
      "Description":"Kuvaus",
      "Dosage":"Dosage",
      "Start Date":"Aloituspäivä",
      "Frequency":"Frequency",
      "End Date":"äättymispäivä",
      "Reminder Time":"Muistutusaika",
      "Add Reminder":"Lisää muistutus",
      "Purpose":"Purpose",
      "Select Frequency":"Taajuus",
      "Pill Reminders":"Pillereiden muistutukset",
      "New Pill Reminder":"Uusi pilleri muistutus"
      /**待补充 */
    }
  }
};

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    resources,
    lng: "en", // language to use, more information here: https://www.i18next.com/overview/configuration-options#languages-namespaces-resources
    // you can use the i18n.changeLanguage function to change the language manually: https://www.i18next.com/overview/api#changelanguage
    // if you're using a language detector, do not define the lng option

    interpolation: {
      escapeValue: false // react already safes from xss
    }
  });

  export default i18n;