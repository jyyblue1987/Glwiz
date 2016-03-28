
// AdbRemoteControlDlg.h : header file
//

#pragma once


// CAdbRemoteControlDlg dialog
class CAdbRemoteControlDlg : public CDialogEx
{
// Construction
public:
	CAdbRemoteControlDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_ADBREMOTECONTROL_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedDialPadRight();	
	afx_msg void OnBnClickedDialPadLeft();
	afx_msg void OnBnClickedNumber9();
	afx_msg void OnBnClickedNumber1();
	afx_msg void OnBnClickedNumber2();
	afx_msg void OnBnClickedNumber3();
	afx_msg void OnBnClickedNumber4();
	afx_msg void OnBnClickedNumber5();
	afx_msg void OnBnClickedNumber6();
	afx_msg void OnBnClickedNumber7();
	afx_msg void OnBnClickedNumber8();
	afx_msg void OnBnClickedVolumeUp();
	afx_msg void OnBnClickedVolumeDown();
	afx_msg void OnBnClickedBack();
	afx_msg void OnBnClickedDialPadCenter();
	afx_msg void OnBnClickedDialPadUp();
	afx_msg void OnBnClickedDialPadDown();
	afx_msg void OnBnClickedHome();
	afx_msg void OnBnClickedMenu();
	afx_msg void OnBnClickedPower();
	afx_msg void OnBnClickedNumber0();
	afx_msg void OnBnClickedPlayPause();
	afx_msg void OnBnClickedPrev();
	afx_msg void OnBnClickedNext();
};
