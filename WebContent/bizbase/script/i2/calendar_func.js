
    // SET THE CALENDAR TO TODAY'S DATE AND DISPLAY THE NEW CALENDAR
    function setToday() {

        // SET GLOBAL DATE TO TODAY'S DATE
        var today = new Date();

        // SET DAY MONTH AND YEAR TO TODAY'S DATE
        var month = today.getMonth() + 1;
        var year = today.getFullYear();
        var day = today.getDate();
        var formatEncoding = document.calControl.DATE_FORMAT_ENCODING.value;

      document.location.href = "calendar.jsp?mon=" + month + "&day=" + day + "&year=" + year+"&DATE_FORMAT_ENCODING="+formatEncoding;
    }

    // SET THE GLOBAL DATE TO THE SELECTED MONTH AND REDRAW THE CALENDAR
    function setCurrentMonth(month) {

        monthArray = new Array('January', 'February', 'March', 'April', 'May', 'June',
                               'July', 'August', 'September', 'October', 'November', 'December');

        // GET THE NEWLY SELECTED MONTH AND CHANGE THE CALENDAR ACCORDINGLY
        document.write(monthArray[parseInt(month,10)-1]);
    }

    // SET THE GLOBAL DATE TO THE PREVIOUS YEAR AND REDRAW THE CALENDAR
    function setPreviousYear() {
      var month = document.calControl.mon.value;
      var day = document.calControl.day.value;
      var year = document.calControl.year.value;
      var formatEncoding = document.calControl.DATE_FORMAT_ENCODING.value;
      if (isFourDigitYear(year) && year > 1000) {
        year--;
        document.location.href = "calendar.jsp?mon=" + month + "&day=" + day + "&year=" + year+"&DATE_FORMAT_ENCODING="+formatEncoding;
      }
    }


    // SET THE GLOBAL DATE TO THE PREVIOUS MONTH AND REDRAW THE CALENDAR
    function setPreviousMonth()
    {
      var month = document.calControl.mon.value;
      var day = document.calControl.day.value;
      var year = document.calControl.year.value;
      var formatEncoding = document.calControl.DATE_FORMAT_ENCODING.value;

      if (isFourDigitYear(year))
      {
        // IF MONTH IS JANUARY, SET MONTH TO DECEMBER AND DECREMENT THE YEAR
        if (parseInt(month,10) == 1) {
          month = 12;
          if (year > 1000) {
            year--;
          }
        }
        else {
          month--;
        }

        document.location.href = "calendar.jsp?mon=" + month + "&day=" + day + "&year=" + year+"&DATE_FORMAT_ENCODING="+formatEncoding;
       }
    }


    // SET THE GLOBAL DATE TO THE NEXT MONTH AND REDRAW THE CALENDAR
    function setNextMonth() {
      var month = document.calControl.mon.value;
      var day = document.calControl.day.value;
      var year = document.calControl.year.value;
      var formatEncoding = document.calControl.DATE_FORMAT_ENCODING.value;

      if (isFourDigitYear(year)) {
        // IF MONTH IS DECEMBER, SET MONTH TO JANUARY AND INCREMENT THE YEAR
        if (parseInt(month,10) == 12) {
          month = 1;
          year++;
        }
        else {
          month++;
        }
        document.location.href = "calendar.jsp?mon=" + month + "&day=" + day + "&year=" + year+"&DATE_FORMAT_ENCODING="+formatEncoding;
        }
    }


    // SET THE GLOBAL DATE TO THE NEXT YEAR AND REDRAW THE CALENDAR
    function setNextYear() {
      var month = document.calControl.mon.value;
      var day = document.calControl.day.value;
      var year = document.calControl.year.value;
      var formatEncoding = document.calControl.DATE_FORMAT_ENCODING.value;

      if (isFourDigitYear(year) && year > 1000) {
        year++;
        document.location.href = "calendar.jsp?mon=" + month + "&day=" + day + "&year=" + year+"&DATE_FORMAT_ENCODING="+formatEncoding;
      }
    }

    function buildDays()
    {
      var month = document.calControl.mon.value;
      var day = document.calControl.day.value;
      var year = document.calControl.year.value;

      var today = new Date();

        // SET DAY MONTH AND YEAR TO TODAY'S DATE
        var today_month = today.getMonth() + 1;
        var today_year = today.getFullYear();
        var today_day = today.getDate();

      var i   = 0;
    var calDoc = "";

      var firstOfMonth = new Date (year, parseInt(month,10)-1, 1);
      var startingPos  = firstOfMonth.getDay();
      var days = getDaysInMonth( month, year);
    var days_previous_month;
    if (month > 1)
      days_previous_month = getDaysInMonth( month - 1, year);
    else
      days_previous_month = getDaysInMonth( 12, year - 1);

    var days_next_month;

    if (month < 12)
      days_next_month = getDaysInMonth( month + 1, year);
    else
      days_next_month = getDaysInMonth( 1, year + 1);


      var blankCell = "<td></td>";

      // GET THE DAY OF THE WEEK THE FIRST DAY OF THE MONTH FALLS ON
      days += startingPos;

      // KEEP TRACK OF THE COLUMNS, START A NEW ROW AFTER EVERY 7 COLUMNS
      var columnCount = 0;

      // MAKE BEGINNING NON-DATE CELLS BLANK
      for (i = 0; i < startingPos; i++) {
        var temp = days_previous_month - (startingPos - i) + 1.;

        calDoc += "<TD width='18px' height='18px' align=center><font color='#b2b2b2' pointsize='11' face='verdana'>" + temp + "</font></TD>";
        columnCount++;
      }

      // SET VALUES FOR DAYS OF THE MONTH
      var currentDay = 0;

      var dayType    = "weekday";

      // DATE CELLS CONTAIN A NUMBER
      for (i = startingPos; i < days; i++)
    {
        var paddingChar = "";

        // ADJUST SPACING SO THAT ALL LINKS HAVE RELATIVELY EQUAL WIDTHS
        if (i-startingPos+1 < 10) {
            padding = "";
        }
        else {
            padding = "";
        }

        // GET THE DAY CURRENTLY BEING WRITTEN
        currentDay = i-startingPos+1;


        // SET THE TYPE OF DAY, THE focusDay GENERALLY APPEARS AS A DIFFERENT COLOR

        if ( currentDay == day )
        {
          cellBGColor = "#FFF274";
        }
        else
        {
          if ((month == today_month) && (year == today_year) && (currentDay == today_day  )) {
            cellBGColor = "#eceef8";
          }
          else
            cellBGColor = "white";
        }


        calDoc += "<TD width='18px' height='18px' align=center bgcolor='" + cellBGColor + "'>" +
                "<A  ondblclick='javascript:onDblClickDate(" +
                currentDay + ")' href='javascript:refreshCal(" +
                currentDay + ")'><font color='#505050' pointsize='11' face='verdana'>" + padding + currentDay +
                paddingChar + "</font></A></TD>";

        columnCount++;

        // START A NEW ROW WHEN NECESSARY
        if (columnCount % 7 == 0) {
            calDoc += "</TR><TR>";
        }
      }

      // MAKE REMAINING NON-DATE CELLS BLANK
      for (i=days; i<42; i++)  {

        var temp = i - days + 1;

        calDoc += "<TD width='18px' height='18px' align=center><font color='#b2b2b2' pointsize='11' face='verdana'>" + temp + "</font></TD>";
        columnCount++;

        // START A NEW ROW WHEN NECESSARY
        if (columnCount % 7 == 0) {
            calDoc += "</TR>";
            if (i<41) {
                calDoc += "<TR>";
            }
        }
      }

      document.write(calDoc);
  }


  // GET NUMBER OF DAYS IN MONTH
  function getDaysInMonth( month, year)  {
      var days;
      // RETURN 31 DAYS
      if (month==1 || month==3 || month==5 || month==7 || month==8 ||
          month==10 || month==12)  {
          days=31;
      }
      // RETURN 30 DAYS
      else if (month==4 || month==6 || month==9 || month==11) {
          days=30;
      }
      // RETURN 29 DAYS
      else if (month==2)  {
          if (isLeapYear(year)) {
              days=29;
          }
          // RETURN 28 DAYS
          else {
              days=28;
          }
      }
      return (days);
  }


  // CHECK TO SEE IF YEAR IS A LEAP YEAR
  function isLeapYear (Year) {

      if (((Year % 4)==0) && ((Year % 100)!=0) || ((Year % 400)==0)) {
          return (true);
      }
      else {
          return (false);
      }
  }


  // SET FIELD VALUE TO THE DATE SELECTED AND REFRESH THE CALENDAR
  function refreshCal(day)
  {
    // SET DAY MONTH AND YEAR TO TODAY'S DATE
    var month = document.calControl.mon.value;
    var year = document.calControl.year.value;
    var formatEncoding = document.calControl.DATE_FORMAT_ENCODING.value;

    document.location.href = "calendar.jsp?mon=" + month + "&day=" + day + "&year=" + year+"&DATE_FORMAT_ENCODING="+formatEncoding;
  }

  function onDblClickDate(day)
{
    // SET DAY MONTH AND YEAR TO TODAY'S DATE
    var month = document.calControl.mon.value;
    var year = document.calControl.year.value;
    var formatEncoding = document.calControl.DATE_FORMAT_ENCODING.value;

    document.location.href = "calendar.jsp?close=true&mon=" + month + "&day=" + day + "&year=" + year+"&DATE_FORMAT_ENCODING="+formatEncoding;
  }

  function onLoadClose()
  {
      returnDate();
  }

  // SET FIELD VALUE TO THE DATE SELECTED AND CLOSE THE CALENDAR WINDOW
    function returnDate()
    {
      var mon = document.calControl.mon.value;
      var day = document.calControl.day.value;
      var year = document.calControl.year.value;

      var formatDate = document.calControl.formattedDate.value;
      // SET THE VALUE OF THE FIELD THAT WAS PASSED TO THE CALENDAR
      var f = top.opener.calDateField;
      f.value = formatDate;
      f.focus();
      var onchangehandler = f.onchange;

      // CLOSE THE CALENDAR WINDOW
      this.close()
      if(onchangehandler != null)
      {
        onchangehandler = f.onchange+"!!!";
        var from = onchangehandler.indexOf("{");
        onchangehandler = onchangehandler.substring(from+1);
        var len = onchangehandler.split(";").length;
        var changeHandlerArray = onchangehandler.split(";");
        for (i=0; i<len-1; i++)
        {
          var changehandler = changeHandlerArray[i] + ";";
          eval("top.opener." + changehandler);
        }
      }
    }



  // ENSURE THAT THE YEAR IS FOUR DIGITS IN LENGTH
  function isFourDigitYear(year) {

    if (year.length != 4) {
        top.newWin.frames['topCalFrame'].document.calControl.year.value = calDate.getFullYear();
        top.newWin.frames['topCalFrame'].document.calControl.year.select();
        top.newWin.frames['topCalFrame'].document.calControl.year.focus();
    }
    else {
        return true;
    }
  }

  // Close the calendar.  Get on with life.
  function closeCalendar() {
    this.close()

  }
