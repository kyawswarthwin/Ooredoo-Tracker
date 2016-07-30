Type=Activity
Version=5.02
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals

End Sub

Sub Globals
	Private edtIMEI As EditText
	Private btnTrack As Button
	Private lblLog As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("IMEITracker")
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub btnTrack_Click
	If edtIMEI.Text.Length = 0 Then
		ToastMessageShow("IMEI Should Not Be Empty.", True)
		Return
	End If
	ProgressDialogShow2("Tracking...", False)
	lblLog.Text = ""
	Dim job As HttpJob
	job.Initialize("Job", Me)
	job.Download2("https://ecareapp.ooredoo.com.mm/deviceapi/Default.aspx", Array As String("Action", "getsubscriberinfo", "IMEI", edtIMEI.Text))
End Sub

Sub JobDone (Job As HttpJob)
	ProgressDialogHide
	If Job.Success Then
		Select Job.JobName
			Case "Job"
				Dim parser As JSONParser
				parser.Initialize(XMLToJSON(Job.GetString))
				Dim root As Map = parser.NextObject
				If root.ContainsKey("Subscriber") Then
					Dim Subscriber As Map = root.Get("Subscriber")
					Dim id As String = Subscriber.Get("id")
					Dim info As List = Subscriber.Get("info")
					For Each colinfo As Map In info
						Dim name As String = colinfo.Get("name")
						Dim value As String = colinfo.Get("value")
						Select name
							Case "imsi"
								lblLog.Text = lblLog.Text & CRLF & "IMSI: " & value
							Case "msisdn"
								lblLog.Text = lblLog.Text & CRLF & "Phone Number: +" & value
							Case "modelname"
								lblLog.Text = lblLog.Text & CRLF & "Model Number: " & value.SubString(value.IndexOf("/") + 1)
						End Select
					Next
				Else
					Dim result As Map = root.Get("result")
					Dim content As String = result.Get("content")
					ToastMessageShow(content, True)
				End If
		End Select
	Else
		ToastMessageShow("Error: " & Job.ErrorMessage, True)
	End If
	Job.Release
End Sub

Sub XMLToJSON(xml As String) As String
	Dim jo As JavaObject
	Dim parser As JSONParser
    Dim generator As JSONGenerator
	
	jo.InitializeNewInstance("org.json.XML", Null)
	Dim json As String = jo.RunMethod("toJSONObject", Array(xml))
    parser.Initialize(json)
    Dim root As Map = parser.NextObject
    generator.Initialize(root)
    Return generator.ToPrettyString(4)
End Sub
