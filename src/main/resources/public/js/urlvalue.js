function GetQueryString(name){
     var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
     var result = window.location.search.substr(1).match(reg);
     return result?decodeURIComponent(result[2]):null;
   }