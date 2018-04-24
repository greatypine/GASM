Sub Align(objname, tblobj, ssobj)
    defn = ssobj.HTMLData
    at1 = InStr(defn, "<col ")
    at2 = InStr(at1, defn, "<tr ")
    newcols = " "
    for j = 0 to tblobj.rows(0).cells.length - 1
      newcols = newcols & "<col width='" & tblobj.rows(0).cells(j).clientWidth-7 & "'> "
    next
    newdefn = Left(defn,at1-1) & newcols & Right(defn, Len(defn)-at2+1)
    ssobj.HTMLData = newdefn
End Sub

Sub Buildit(objname, tblobj, ssobj)
    defn = "<html xmlns:x=&quot;urn:schemas-microsoft-com:office:excel&quot;><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:WorksheetOptions><x:DoNotDisplayTitleBar/><x:DoNotDisplayToolBar/><x:ProtectContents>True</x:ProtectContents><x:ProtectObjects>False</x:ProtectObjects><x:ProtectScenarios>False</x:ProtectScenarios><x:ViewableRange>A1:" & Chr(65+tblobj.rows(0).cells.length-1) & tblobj.rows.length & "</x:ViewableRange></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><style>.header{background-color:#d1d6f0;border-top:solid #999999 1px;border-left:solid #999999 1px;border-right:solid #999999 1px;border-bottom:solid #999999 1px;}.cellodd{background-color:#f7f8fd;border-top:solid #999999 1px;border-left:solid #999999 1px;border-right:solid #999999 1px;border-bottom:solid #999999 1px;} .celleven{background-color:#eceef8;border-top:solid #999999 1px;border-left:solid #999999 1px;border-right:solid #999999 1px;border-bottom:solid #999999 1px;} .unlocked {mso-protection:unlocked visible;font-weight:bold;}</style><table>"
    for i = 0 to tblobj.rows.length - 1
      defn = defn & "<tr>"
      for j = 0 to tblobj.rows(0).cells.length - 1
        if tblobj.rows(i).className = "tableColumnHeadings" then
          defn = defn & "<td class=&quot;header&quot;>" & tblobj.rows(i).cells(j).innerText
        else
        if tblobj.rows(i).Cells(j).id = "" then
          defn = defn & "<td class=&quot;unlocked&quot;>" & tblobj.rows(i).cells(j).innerText
        else
        if tblobj.rows(i).Cells(j).id = "readonly" then
          defn = defn & "<td class=&quot;celleven&quot;>" & tblobj.rows(i).cells(j).innerText
        else
        if Int(i/2) = (i / 2) then
          defn = defn & "<td class=&quot;celleven&quot; x:fmla=&quot;" & tblobj.rows(i).cells(j).id & "&quot;>"
        else
          defn = defn & "<td class=&quot;cellodd&quot; x:fmla=&quot;" & tblobj.rows(i).cells(j).id & "&quot;>"
        end if
        end if
        end if
        end if
        defn = defn & "</td>"
      next
      defn = defn & "</tr>"
    next
    defn = defn & "</table></html>"
    ssobj.HTMLData = defn
End Sub

Sub PopulateSpreadsheet(objname, tblobj, ssobj)
    ssobj.ActiveSheet.Cells(1,1).Select
    for i = 1 to tblobj.rows.length - 1
      if tblobj.rows(i).className <> "tableColumnHeadings" then
        for j = 0 to tblobj.rows(0).cells.length - 1
          if tblobj.rows(i).Cells(j).id = "" then
            ssobj.ActiveSheet.Range(Chr(65+j) & (1 + i)).Value = tblobj.rows(i).cells(j).innerText
          end if
        next
      end if
    next
End Sub

Sub PopulateTable(objname, tblobj, ssobj)
    for i = 0 to tblobj.rows.length - 1
      if tblobj.rows(i).className <> "tableColumnHeadings" then
        for j = 0 to tblobj.rows(0).cells.length - 1
          tblobj.rows(i).cells(j).innerText = ssobj.ActiveSheet.Range(Chr(65+j) & (1 + i)).Value
        next
      end if
    next
End Sub

Sub ExportTable(objname, tblobj, ssobj)
    ssobj.ActiveSheet.Export()
End Sub

Sub DumpTable(ssobj, obj)
    //MsgBox ssobj.XMLData
    obj.value = ssobj.XMLData2
End Sub

Sub SortTable(objname, tblobj, ssobj)
  // to sort
  // 1. get htmldata
  // 2. change protection
  // 3. write htmldata
  // 4. sort
  // 5. get htmldata
  // 6. change protection
  // 7. rewrite htmldata

  if ssobj.Selection.Row = 1 then
    //LPM need to see if entire column selected
    //MsgBox ssobj.Selection.RowCount

    col = ssobj.Selection.Column

    if i2uiSortColumn = col then
      if i2uiSortDirection = 0 then
        i2uiSortDirection = 1
      else
        i2uiSortDirection = 0
      end if
    else
        i2uiSortDirection = 0
    end if
    i2uiSortColumn = col

    search1 = "<x:ProtectContents>True</x:ProtectContents>"
    search2 = "<x:ProtectContents>False</x:ProtectContents>"
    defn = ssobj.HTMLData
    at1 = InStr(defn, search1)
    newdefn = Left(defn,at1-1) & search2 & Right(defn, Len(defn)-at1-Len(search1))
    ssobj.HTMLData = newdefn

    //NOTE : this works for single row header only
    ssobj.ActiveSheet.UsedRange.Sort col,i2uiSortDirection,1

    defn = ssobj.HTMLData
    at1 = InStr(defn, search2)
    newdefn = Left(defn,at1-1) & search1 & Right(defn, Len(defn)-at1-Len(search2))
    ssobj.HTMLData = newdefn
  else
    MsgBox "You must first select the column you wish to sort"
  end if
End Sub

