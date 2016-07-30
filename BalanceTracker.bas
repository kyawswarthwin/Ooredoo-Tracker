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
	Private edtPhoneNumber As EditText
	Private btnTrack As Button
	Private lblLog As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("BalanceTracker")
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub btnTrack_Click
	If edtPhoneNumber.Text.Length = 0 Then
		ToastMessageShow("Phone Number Should Not Be Empty.", True)
		Return
	End If
	ProgressDialogShow2("Tracking...", False)
	lblLog.Text = ""
	Dim job As HttpJob
	job.Initialize("Job", Me)
	job.Download2("http://ecareapp.ooredoo.com.mm/pageapi.aspx", Array As String("action", "apiquerybalance", "ws", "ecare", "userid", "95" & edtPhoneNumber.Text.SubString(1)))
End Sub

Sub JobDone (Job As HttpJob)
	ProgressDialogHide
	If Job.Success Then
		Select Job.JobName
			Case "Job"
				Try
					Dim parser As JSONParser
					parser.Initialize(Job.GetString)
					Dim root As Map = parser.NextObject
					Dim xml As Map = root.Get("xml")
					Dim status_code As String = xml.Get("status_code")
					Dim status_desc As String = xml.Get("status_desc")
					If status_code = "0" Then
						Dim mobileno As String = xml.Get("mobileno")
						Dim balance As Map = xml.Get("balance")
						Dim amount As String = balance.Get("amount")
						Dim expiretime As String = balance.Get("expiretime")
						Dim packtype As String = balance.Get("packtype")
						Dim desc As String = balance.Get("desc")
						lblLog.Text = desc & CRLF & "    Amount: " & amount & CRLF & "    Expiry Date: " & expiretime
						Dim data As Map = xml.Get("data")
						Dim amount As String = data.Get("amount")
						Dim expiretime As String = data.Get("expiretime")
						Dim packtype As String = data.Get("packtype")
						Dim desc As String = data.Get("desc")
						lblLog.Text = lblLog.Text & CRLF & CRLF & desc & CRLF & "    Amount: " & amount & CRLF & "    Expiry Date: " & expiretime
					Else
						ToastMessageShow(status_desc, True)
					End If
				Catch
					Log("Error: " & LastException)
				End Try
		End Select
	Else
		ToastMessageShow("Error: " & Job.ErrorMessage, True)
	End If
	Job.Release
End Sub
