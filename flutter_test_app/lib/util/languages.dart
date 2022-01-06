library my_prj.globals;

String selectedLang = "eng";
String selectedOrdr = "alph"; //alph or date

Map<String, Map> stringLang = {
    "eng" : {
      
      //Activity Page
      "lastTaskWorked"  : "Last Task Worked",
      "projectDuration" : "Project Duration",
      "started" : "Started:",
      "lastWorked"  : "Last Worked:",
      "startedTile" : "Started:",
      "toTile"  : "To:",
      "home" : "Home",
      "NotStartedYet" : "Not Started Yet",
      "NoWorkingSessionYet": "No Working Session Yet",

      //AddActivity Page
      "titlePage" : "New Activity",
      "projectOption" : "Project",
      "taskOption"  : "Task",
      "nameTip" : "Name",
      "directoryTip"  : "Directory",
      "tagTip"  : "Tag (optional)",
      "saveTip" : "Save",
      "savemsg" : "Creating",
      "errorName" : "Please enter a name",
      "errorDirectory" : "Please select the project directory",
      "optional" : "(optional)",

      //Intervals Page 
      "lastTimeWorkedIntervals" : "Last Time Worked",
      "totalDuration" : "Total Duration",
      "startedOn" : "Started on:",
      "fromIntervals" : "From:",
      "toIntervals" : "To:",
      "currentWorkingPeriod"  : "Current Working Period",

      //Search Page
      "searchByTag" : "Search by tag...",
      "tags"  : "Tags:",
      "waiting" : "Waiting for tag search...",

      //Drawer
      "drawerHeader" : "Select App Language",
      "dropdownhint" : "Select language",
      "eng" : "English",
      "cat" : "Catalan",

      //Splash Screen
      "welcome": "Welcome to",
      "welcomemsg" : "We see you don't have any project or task yet.\n\nStart using the App creating your first activity!",

      "formatDate" : "MM/dd/yy - HH:mm:ss",
      "orderSelect" : "Select the elements on screen order",
      "orderDate" : "Date",
      "orderName" : "Name",

    },

    "cat" : {

      //Activity Page
      "lastTaskWorked"  : "Última Tasca Treballada",
      "projectDuration" : "Duració del Projecte",
      "started" : "Inici:",
      "lastWorked"  : "Últim Període:",
      "startedTile" : "Inici:",
      "toTile"  : "Final:",
      "home" : "Inici",
      "NotStartedYet" : "Sense Inici",
      "NoWorkingSessionYet": "Sense Sessions",
      
      //AddActivity Page
      "titlePage" : "Nova Activitat",
      "projectOption" : "Projecte",
      "taskOption"  : "Tasca",
      "nameTip" : "Nom",
      "directoryTip"  : "Directori",
      "tagTip"  : "Etiqueta (opcional)",
      "saveTip" : "Desa",
      "savemsg" : "Creant",
      "errorName" : "Si us plau, introdueix un nom",
      "errorDirectory" : "Si us plau, selecciona un directori projecte",
      "optional" : "(opcional)",

      //Intervals Page 
      "lastTimeWorkedIntervals" : "Últim Cop Treballat",
      "totalDuration" : "Duració Total",
      "startedOn" : "Començat a:",
      "fromIntervals" : "Inici:",
      "toIntervals" : "Final:",
      "currentWorkingPeriod"  : "Període de Treball Actual",

      //Search Page
      "searchByTag" : "Cerca per Etiqueta...",
      "tags"  : "Etiquetes: ",
      "waiting" : "Introdueix una etiqueta per començar...",

      //Drawer
      "drawerHeader" : "Selecciona l'idioma de l'App",
      "dropdownhint" : "Selecciona l'idioma",
      "eng" : "Anglès",
      "cat" : "Català",

      //Splash Screen
      "welcome": "Bienvenido a",
      "welcomemsg" : "Veiem que encara no tens cap projecte o tasca encara.\nComença a gaudir de l'aplicació creant la teva primera activitat!",

      "formatDate" : "dd/MM/yy - HH:mm:ss",
      "orderSelect" : "Selecciona l'ordre dels elements en pantalla",
      "orderDate" : "Data",
      "orderName" : "Nom",
    }

};