
// AdbRemoteControlDlg.cpp : implementation file
//

#include "stdafx.h"
#include "AdbRemoteControl.h"
#include "AdbRemoteControlDlg.h"
#include "afxdialogex.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CAboutDlg dialog used for App About

class CAboutDlg : public CDialogEx
{
public:
	CAboutDlg();

// Dialog Data
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

// Implementation
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialogEx(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialogEx)
END_MESSAGE_MAP()


// CAdbRemoteControlDlg dialog



CAdbRemoteControlDlg::CAdbRemoteControlDlg(CWnd* pParent /*=NULL*/)
	: CDialogEx(CAdbRemoteControlDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CAdbRemoteControlDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAdbRemoteControlDlg, CDialogEx)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDC_DIAL_PAD_RIGHT, &CAdbRemoteControlDlg::OnBnClickedDialPadRight)	
	ON_BN_CLICKED(IDC_DIAL_PAD_LEFT, &CAdbRemoteControlDlg::OnBnClickedDialPadLeft)
	ON_BN_CLICKED(IDC_NUMBER_9, &CAdbRemoteControlDlg::OnBnClickedNumber9)
	ON_BN_CLICKED(IDC_NUMBER_1, &CAdbRemoteControlDlg::OnBnClickedNumber1)
	ON_BN_CLICKED(IDC_NUMBER_2, &CAdbRemoteControlDlg::OnBnClickedNumber2)
	ON_BN_CLICKED(IDC_NUMBER_3, &CAdbRemoteControlDlg::OnBnClickedNumber3)
	ON_BN_CLICKED(IDC_NUMBER_4, &CAdbRemoteControlDlg::OnBnClickedNumber4)
	ON_BN_CLICKED(IDC_NUMBER_5, &CAdbRemoteControlDlg::OnBnClickedNumber5)
	ON_BN_CLICKED(IDC_NUMBER_6, &CAdbRemoteControlDlg::OnBnClickedNumber6)
	ON_BN_CLICKED(IDC_NUMBER_7, &CAdbRemoteControlDlg::OnBnClickedNumber7)
	ON_BN_CLICKED(IDC_NUMBER_8, &CAdbRemoteControlDlg::OnBnClickedNumber8)
	ON_BN_CLICKED(IDC_VOLUME_UP, &CAdbRemoteControlDlg::OnBnClickedVolumeUp)
	ON_BN_CLICKED(IDC_VOLUME_DOWN, &CAdbRemoteControlDlg::OnBnClickedVolumeDown)
	ON_BN_CLICKED(IDC_BACK, &CAdbRemoteControlDlg::OnBnClickedBack)
	ON_BN_CLICKED(IDC_DIAL_PAD_CENTER, &CAdbRemoteControlDlg::OnBnClickedDialPadCenter)
	ON_BN_CLICKED(IDC_DIAL_PAD_UP, &CAdbRemoteControlDlg::OnBnClickedDialPadUp)
	ON_BN_CLICKED(IDC_DIAL_PAD_DOWN, &CAdbRemoteControlDlg::OnBnClickedDialPadDown)
	ON_BN_CLICKED(IDC_HOME, &CAdbRemoteControlDlg::OnBnClickedHome)
	ON_BN_CLICKED(IDC_MENU, &CAdbRemoteControlDlg::OnBnClickedMenu)
	ON_BN_CLICKED(IDC_POWER, &CAdbRemoteControlDlg::OnBnClickedPower)
	ON_BN_CLICKED(IDC_NUMBER_0, &CAdbRemoteControlDlg::OnBnClickedNumber0)
END_MESSAGE_MAP()


// CAdbRemoteControlDlg message handlers

BOOL CAdbRemoteControlDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		BOOL bNameValid;
		CString strAboutMenu;
		bNameValid = strAboutMenu.LoadString(IDS_ABOUTBOX);
		ASSERT(bNameValid);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CAdbRemoteControlDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialogEx::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CAdbRemoteControlDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialogEx::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CAdbRemoteControlDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}



void CAdbRemoteControlDlg::OnBnClickedDialPadRight()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 22"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedDialPadLeft()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 21"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedDialPadUp()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 19"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedDialPadDown()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 20"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedDialPadCenter()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 23"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedHome()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 3"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedNumber0()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 7"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedNumber1()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 8"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedNumber2()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 9"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedNumber3()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 10"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedNumber4()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 11"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedNumber5()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 12"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedNumber6()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 13"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedNumber7()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 14"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedNumber8()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 15"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedNumber9()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 16"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedVolumeUp()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 24"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedVolumeDown()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 25"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedBack()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 4"), NULL, SW_HIDE);
}

void CAdbRemoteControlDlg::OnBnClickedMenu()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 1"), NULL, SW_HIDE);
}


void CAdbRemoteControlDlg::OnBnClickedPower()
{
	ShellExecute(GetSafeHwnd(), _T("open"), _T("adb.exe"), _T("shell input keyevent 3"), NULL, SW_HIDE);
}


