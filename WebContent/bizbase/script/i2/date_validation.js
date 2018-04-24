function dateAdd(inDate, duration,inFormat, outFormat)
{
   if (inFormat == null) inFormat = page_dateFormat;
   if (outFormat == null) outFormat = inFormat;
   // To EXSL Format
   xsDate1 = parseDate(inDate,inFormat, 'xs:dateTime', null);
   // Add Duration
   xsDate2 = add(xsDate1,duration); // date.js
   // To UI Format
   return formatDate(xsDate2,outFormat, null , null);

}
function dateConvert(date, format1, format2)
{
   // To EXSL Format
   xsDate = parseDate(date, format1, 'xs:dateTime', null);

   // To UI Format
   return formatDate(xsDate,format2, null , null);
}
function dateIsValid(inDate, inFormat)
{
  if (inFormat == null) inFormat = page_dateFormat;
  // To EXSL Format
  try
  {
    xsDate1 = parseDate(inDate,inFormat, 'xs:dateTime', null);
  }
  catch(e)
  {
    return false;
  }
  if (inDate != '' && xsDate1 == '') return false;
  return true;
}

 function dateDifferenceInDays(inDate1, inDate2,inFormat)
{
   if (inFormat == null) inFormat = page_dateFormat;
    if (inDate1 == '') return 0;
    if (inDate2 == '') return 0;

   // To EXSL Format
   xsDate1 = parseDate(inDate1,inFormat, 'xs:dateTime', null);
   xsDate2 = parseDate(inDate2,inFormat, 'xs:dateTime', null);
   diff = difference(xsDate1,xsDate2);
   if (diff == 0) return 0;

   day = diff.substring(diff.indexOf("P") + 1, diff.indexOf("D"));

   if (diff.indexOf("-") == 0)day = day*-1;

   return day;

}

function dateGetFormat(val)
{
  if (dateIsValid(val,page_dateTimeFormat)) return  page_dateTimeFormat;
  if (dateIsValid(val,page_dateFormat)) return  page_dateFormat;
  if (dateIsValid(val,page_timeFormat)) return  page_timeFormat;
}
/*Use this instead of compareDates() function below*/
function datecompare(inDate1,inFormat1,inDate2,inFormat2)
{
  var xsFormat1 ="xs:date";
  var xsFormat2 ="xs:date";
  if (inFormat1 == null || inFormat1 == '' || inFormat1==page_dateFormat)
  {
    inFormat1 = page_dateFormat ;
    xsFormat1 = "xs:date";
  }
  if (inFormat2 == null || inFormat2 == '' || inFormat2 == page_dateFormat)
  {
    inFormat2 = page_dateFormat ;
    xsFormat2 = "xs:date";
  }

  if (inFormat1 == 'DateTime' || inFormat1 == page_dateTimeFormat)
  {
    inFormat1 = page_dateTimeFormat ;
    xsFormat1 = "xs:dateTime";
  }
  if (inFormat2 == 'DateTime' || inFormat2 == page_dateTimeFormat)
  {
    inFormat2 = page_dateTimeFormat;
    xsFormat2 = "xs:dateTime";

  }

   if (inDate1 == '') return -2;
   if (inDate2 == '') return -2;

   // To EXSL Format
   xsDate1 = parseDate(inDate1,inFormat1, xsFormat1, null);
   xsDate2 = parseDate(inDate2,inFormat2, xsFormat2, null);

   diff = difference(xsDate1,xsDate2);

   if (diff == 0) return 0;
   if (diff.indexOf("-") == 0) return 1;
   return -1;
}
function dateCurrent(outFormat)
{

  if (page_dateCurrent != '')
  {
   if (outFormat == null)
   return  page_dateCurrent;
   else
   return parseDate(page_dateCurrent,page_dateFormat, outFormat, null);
  }
  else
  {
   if (outFormat == null) outFormat = page_dateFormat;
   return formatDate(date(),outFormat, null , null);
  }
}
/*
 The implementation in this file from this point onwards
 will be later replaced by the date.js functions.
 This implementation is home grown and
 will not work for all JDK SimpleDateFormat.
*/


function isDate(val,format)
{
  var date=getDateFromFormat(val,format);
  if (date==0) { return false; }
  return true;
}

/* Dont Use this instead use dateCompare() above*/
function compareDates(date1,dateformat1,date2,dateformat2)
{
  var d1=getDateFromFormat(date1,dateformat1);
  var d2=getDateFromFormat(date2,dateformat2);
  if (d1==0 || d2==0)
  {
    return -2;
  }
  else if (d1 > d2)
  {
    return 1;
  }
  else if (d1 < d2)
  {
    return -1;
  }
  return 0;
}

function getDateFromFormat(val, format, zerofill)
{
  val=val+"";
  format=format+"";
  var i_val=0;
  var i_format=0;
  var c="";
  var token="";
  var token2="";
  var x,y;
  var now=new Date();
  var year=zerofill ? 0 : now.getYear();
  var month=zerofill ? 0 : now.getMonth()+1;
  var date=now.getDate();
  var hh=zerofill ? 0 : now.getHours();
  var mm=zerofill ? 0 : now.getMinutes();
  var ss=zerofill ? 0 : now.getSeconds();
  var ampm="";
  var timezone="";
  var MONTH_NAMES= new Array("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

  while (i_format < format.length)
  {
    // Get next token from format string
    c=format.charAt(i_format);
    token="";
    while ((format.charAt(i_format)==c) && (i_format < format.length))
    {
     token += format.charAt(i_format++);
    }
    // Extract contents of value based on format token
    if (token=="yyyy" || token=="yy" || token=="y")
    {
      if (token=="yyyy") { x=4;y=4; }
      if (token=="yy")   { x=2;y=2; }
      if (token=="y")    { x=2;y=4; }
      year=_getInt(val,i_val,x,y);
      if (year==null) { return 0; }
      i_val += year.length;
      if (year.length==2)
      {
        if (year > 70) { year=1900+(year-0); }
        else { year=2000+(year-0); }
      }
    }

    else if (token=="MMM")
    {
      month=0;
      for (var i=0; i<MONTH_NAMES.length; i++)
        {
        var month_name=MONTH_NAMES[i];
        if (val.substring(i_val,i_val+month_name.length).toLowerCase()==month_name.toLowerCase())
          {
          month=i+1;
          if (month>12) { month -= 12; }
          i_val += month_name.length;
          break;
        }
      }
      if ((month < 1)||(month>12)){return 0;}
    }

    else if (token=="MM"||token=="M")
    {
      month=_getInt(val,i_val,token.length,2);
      if(month==null||(month<1)||(month>12)){return 0;}
      i_val+=month.length;
    }

    else if (token=="dd"||token=="d")
    {
      date=_getInt(val,i_val,token.length,2);
      if(date==null||(date<1)||(date>31)){return 0;}
      i_val+=date.length;
    }

    else if (token=="hh"||token=="h")
    {
      hh=_getInt(val,i_val,token.length,2);
      if(hh==null||(hh<1)||(hh>12)){return 0;}
      i_val+=hh.length;
    }

    else if (token=="HH"||token=="H")
    {
      hh=_getInt(val,i_val,token.length,2);
      if(hh==null||(hh<0)||(hh>23)){return 0;}
      i_val+=hh.length;
    }

    else if (token=="KK"||token=="K")
    {
      hh=_getInt(val,i_val,token.length,2);
      if(hh==null||(hh<0)||(hh>11)){return 0;}
      i_val+=hh.length;
    }

    else if (token=="kk"||token=="k")
    {
      hh=_getInt(val,i_val,token.length,2);
      if(hh==null||(hh<1)||(hh>24)){return 0;}
      i_val+=hh.length;hh--;
    }

    else if (token=="mm"||token=="m")
    {
      mm=_getInt(val,i_val,token.length,2);
      if(mm==null||(mm<0)||(mm>59)){return 0;}
      i_val+=mm.length;
    }

    else if (token=="ss"||token=="s")
    {
      ss=_getInt(val,i_val,token.length,2);
      if(ss==null||(ss<0)||(ss>59)){return 0;}
      i_val+=ss.length;
    }

    else if (token=="a")
    {
     if (val.substring(i_val,i_val+2).toLowerCase()=="am") {ampm="AM";}
     else if (val.substring(i_val,i_val+2).toLowerCase()=="pm") {ampm="PM";}
     else {return 0;}
     i_val+=2;
    }

    else if (token=="z" || token=="Z")
    {
      timezoneSize = val.length - i_val;
      timezone = val.substring(i_val,val.length);
     i_val+=timezoneSize;
    }

    else
    {
     if (val.substring(i_val,i_val+token.length)!=token) {return 0;}
     else {i_val+=token.length;}
   }

  }


 // If there are any trailing characters left in the value, it doesn't match
  if (i_val != val.length) { return 0; }
 // Is date valid for month?

  if (month==2)
  {
    // Check for leap year
    if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
    if (date > 29){ return false; }
  }
  else
  {
    if (date > 28) { return false; } }
  }

  if ((month==4)||(month==6)||(month==9)||(month==11))
  {
    if (date > 30) { return false; }
  }

  // Correct hours value
  if (hh<12 && ampm=="PM") { hh = parseInt(hh); hh+=12; }
  else if (hh>11 && ampm=="AM") { hh-=12; }

  var newdate=new Date(year,month-1,date,hh,mm,ss);

  return newdate.getTime();
}

function _isInteger(val)
{
  var digits="1234567890";
  for (var i=0; i < val.length; i++)
  {
    if (digits.indexOf(val.charAt(i))==-1) { return false; }
  }
  return true;
}

function _getInt(str,i,minlength,maxlength)
{
  for (var x=maxlength; x>=minlength; x--)
  {
    var token=str.substring(i,i+x);
    if (token.length < minlength) { return null; }
    if (_isInteger(token)) { return token; }
  }
  return null;
}

function getDatePattern(oField)
{
  var fieldName = oField.name;
  var fieldNameSize = fieldName.length;
  var pattern;
  
  if (fieldNameSize > 5 && fieldName.charAt(fieldNameSize - 5) == '_')
  {
    var datePattern = null;
    var timePattern = null;
    var c1 = fieldName.charAt(fieldNameSize - 4);
    var c2 = fieldName.charAt(fieldNameSize - 3);
    if (c1 == 'D')
    {
      switch (c2)
      {
        case 'S':
                 datePattern = page_shortDateFormat;
                 break;
        case 'M':
                 datePattern = page_mediumDateFormat;
                 break;
        case 'L':
                 datePattern = page_longDateFormat;
                 break;
        case 'F':
                 datePattern = page_fullDateFormat;
                 break;               
        default: 
                 break;
      }

      if (datePattern != null)
      {
        var c3 = fieldName.charAt(fieldNameSize - 2);
        var c4 = fieldName.charAt(fieldNameSize - 1);
        if (c3 == 'T')
        {
          switch (c4)
          {
            case 'S':
                     timePattern = page_shortTimeFormat;
                     break;
            case 'M':
                     timePattern = page_mediumTimeFormat;
                     break;
            case 'L':
                     timePattern = page_longTimeFormat;
                     break;
            case 'F':
                     timePattern = page_fullTimeFormat;
                     break;               
            default:   
                     break;
          }
        }
        if (timePattern != null)
        {
          pattern = datePattern+" "+timePattern;
        }
        else
        {
          // Not a valid time
          pattern = getShortDatePattern(oField);
        }
      }
      else
      {
        // Not a valid date
        pattern = getShortDatePattern(oField);
      }
    }
    else
    {
      // Not a valid DateTime pattern
      pattern = getShortDatePattern(oField);
    }
  }
  else
  {
    // Not a valid DateTime pattern
    pattern = getShortDatePattern(oField);
  }
  return pattern;
}

function getShortDatePattern(oField)
{
  var fieldName = oField.name;
  var fieldNameSize = fieldName.length;
  var pattern;

  if (fieldNameSize > 3 && fieldName.charAt(fieldNameSize - 3) == '_')
  {
    var c1 = fieldName.charAt(fieldNameSize - 2);
    var c2 = fieldName.charAt(fieldNameSize - 1);

    if (c1 == 'D')
    {
      switch (c2)
      {
        case 'M':
                 pattern = page_mediumDateFormat;
                 break;
        case 'L':
                 pattern = page_longDateFormat;
                 break;
        case 'F':
                 pattern = page_fullDateFormat;
                 break;
        case 'T':
                 pattern = page_shortDateFormat+" "+page_shortTimeFormat;
                 break;
               
        case 'S':
        case 'C':
        default:         
                 pattern = page_shortDateFormat;
                 break;
      }
    }
    else if (c1 == 'T')
    {
      switch (c2)
      {
        case 'M':
                 pattern = page_mediumTimeFormat;
                 break;
        case 'L':
                 pattern = page_longTimeFormat;
                 break;
        case 'F':
                 pattern = page_fullTimeFormat;
                 break;               
        case 'S':
        default:         
                 pattern = page_shortTimeFormat;
                 break;
      }
    }
  }
  return pattern;
}

function getDateFormatEncoding(oField)
{
  var fieldName = oField.name;
  var fieldNameSize = fieldName.length;
  var dateEncoding;
  
  if (fieldNameSize > 5 && fieldName.charAt(fieldNameSize - 5) == '_')
  {
    var c1 = fieldName.charAt(fieldNameSize - 4);
    if (c1 == 'D')
    {
      dateEncoding = fieldName.substring(fieldNameSize-4, fieldNameSize);
    }
  }
  else if (fieldNameSize > 3 && fieldName.charAt(fieldNameSize - 3) == '_')
  {
    var c1 = fieldName.charAt(fieldNameSize - 2);

    if ((c1 == 'D') || (c1 == 'T'))
    {
      dateEncoding = fieldName.substring(fieldNameSize-2, fieldNameSize);
    }
  }
  return dateEncoding;
}



