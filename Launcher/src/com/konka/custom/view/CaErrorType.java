package com.konka.custom.view;


public  class CaErrorType 
{
	/*---------- CAS提示信息 ---------*/
	public static  enum CA_NOTIFY
	{
		ST_CA_MESSAGE_CANCEL_TYPE, /* 取消当前的显示 */
		ST_CA_MESSAGE_BADCARD_TYPE, /* 无法识别卡 */
		ST_CA_MESSAGE_EXPICARD_TYPE,  /* 智能卡过期，请更换新卡 */
		ST_CA_MESSAGE_INSERTCARD_TYPE,  /* 加扰节目，请插入智能卡 */
		ST_CA_MESSAGE_NOOPER_TYPE,  /* 卡中不存在节目运营商 */
		ST_CA_MESSAGE_BLACKOUT_TYPE,  /* 条件禁播 */
		ST_CA_MESSAGE_OUTWORKTIME_TYPE,  /* 当前时段被设定为不能观看 */
		ST_CA_MESSAGE_WATCHLEVEL_TYPE,  /* 节目级别高于设定的观看级别 */
		ST_CA_MESSAGE_PAIRING_TYPE,  /* 智能卡与本机顶盒不对应 */
		ST_CA_MESSAGE_NOENTITLE_TYPE,  /* 没有授权 */
		ST_CA_MESSAGE_DECRYPTFAIL_TYPE,  /* 节目解密失败 */
		ST_CA_MESSAGE_NOMONEY_TYPE,  /* 卡内金额不足 */
		ST_CA_MESSAGE_ERRREGION_TYPE,  /* 区域不正确 */
		ST_CA_MESSAGE_NEEDFEED_TYPE,  /* 子卡需要和母卡对应，请插入母卡 */
		ST_CA_MESSAGE_ERRCARD_TYPE,  /* 智能卡校验失败，请联系运营商 */
		ST_CA_MESSAGE_UPDATE_TYPE,  /* 智能卡升级中，请不要拔卡或者关机 */
		ST_CA_MESSAGE_LOWCARDVER_TYPE,  /* 请升级智能卡 */
		ST_CA_MESSAGE_VIEWLOCK_TYPE,  /* 请勿频繁切换频道 */
		ST_CA_MESSAGE_MAXRESTART_TYPE,  /* 智能卡暂时休眠，请5分钟后重新开机 */
		ST_CA_MESSAGE_FREEZE_TYPE,  /* 智能卡已冻结，请联系运营商 */
		ST_CA_MESSAGE_CALLBACK_TYPE,  /* 智能卡已暂停，请回传收视记录给运营商 */
		ST_CA_MESSAGE_CURTAIN_TYPE, /*窗帘节目，不可预览阶段*/
		ST_CA_MESSAGE_CARDTESTSTART_TYPE, /*升级测试卡测试中...*/
		ST_CA_MESSAGE_CARDTESTFAILD_TYPE, /*升级测试卡测试失败，请检查机卡通讯模块*/
		ST_CA_MESSAGE_CARDTESTSUCC_TYPE, /*升级测试卡测试成功*/
		ST_CA_MESSAGE_NOCALIBOPER_TYPE,/*卡中不存在移植库定制运营商*/
		ST_CA_MESSAGE_STBLOCKED_TYPE,  /* 请重启机顶盒 */
		ST_CA_MESSAGE_STBFREEZE_TYPE,  /* 机顶盒被冻结 */
		ST_CA_MESSAGE_Reserved_1,  /* 预留1 */
		ST_CA_MESSAGE_Reserved_2,  /* 预留2 */
		ST_CA_MESSAGE_Reserved_3,  /* 预留3 */
		ST_CA_MESSAGE_Reserved_4,  /* 预留4 */
		ST_CA_MESSAGE_Reserved_5,  /* 预留5 */
		ST_CA_MESSAGE_Reserved_6,  /* 预留6 */
		ST_CA_MESSAGE_Reserved_7,  /* 预留7 */
		ST_CA_MESSAGE_Reserved_8,  /* 预留8 */	
		ST_CA_MESSAGE_Reserved_9,  /* 预留9 */
		ST_CA_MESSAGE_Reserved_10,  /* 预留10 */
		ST_CA_MESSAGE_Reserved_11,  /* 预留11*/
		ST_CA_MESSAGE_Reserved_12,  /* 预留12*/
		ST_CA_MESSAGE_Reserved_13,  /* 预留13*/
		ST_CA_MESSAGE_Reserved_14,  /* 预留14*/
		ST_CA_MESSAGE_Reserved_15,  /* 预留15*/
		ST_CA_MESSAGE_Reserved_16,  /* 预留16*/
		ST_CA_MESSAGE_Reserved_17,  /* 预留17*/
		ST_CA_MESSAGE_Reserved_18,  /* 预留18*/
		ST_CA_MESSAGE_Reserved_19,  /* 预留19*/
		ST_CA_MESSAGE_Reserved_20,  /* 预留20*/
		ST_CA_MESSAGE_Reserved_21,  /* 预留21*/
		ST_CA_MESSAGE_Reserved_22,  /* 预留22*/
		ST_STB_MESSAGE_NOSIGNAL,  /* 信号中断--0-50*/
		ST_STB_MESSAGE_NOPROG,  /* 没有节目，请搜索*/
	}
	
	/*---------- 功能调用返回值定义 ----------*/
	public enum RETURN_CODE
	{
		ST_CA_RC_OK(0),                  /* 成功 */
		ST_CA_RC_UNKNOWN(1),             /* 未知错误 */
		ST_CA_RC_POINTER_INVALID(2),     /* 指针无效 */
		ST_CA_RC_CARD_INVALID(3),       /* 智能卡无效 */
		ST_CA_RC_PIN_INVALID(4),         /* PIN码无效 */
		ST_CA_RC_DATASPACE_SMALL(6),     /* 所给的空间不足 */
		ST_CA_RC_CARD_PAIROTHER(7),      /* 智能卡已经对应别的机顶盒 */
		ST_CA_RC_DATA_NOT_FIND(8),       /* 没有找到所要的数据 */
		ST_CA_RC_PROG_STATUS_INVALID(9), /* 要购买的节目状态无效 */
		ST_CA_RC_CARD_NO_ROOM(10),        /* 智能卡没有空间存放购买的节目 */
		ST_CA_RC_WORKTIME_INVALID(11),    /* 设定的工作时段无效 */
		ST_CA_RC_IPPV_CANNTDEL(12),       /* IPPV节目不能被删除 */
		ST_CA_RC_CARD_NOPAIR(13),         /* 智能卡没有对应任何的机顶盒 */
		ST_CA_RC_WATCHRATING_INVALID(14), /* 设定的观看级别无效 */
		ST_CA_RC_CARD_NOTSUPPORT(15),     /* 当前智能卡不支持此功能 */
		ST_CA_RC_DATA_ERROR(16),          /* 数据错误，智能卡拒绝 */
		ST_CA_RC_FEEDTIME_NOT_ARRIVE(17), /* 喂养时间未到，子卡不能被喂养 */
		ST_CA_RC_CARD_TYPEERROR(18),      /* 子母卡喂养失败，插入智能卡类型错误 */
		ST_CA_RC_CAS_FAILED(32),          //发卡cas指令执行失败
		ST_CA_RC_OPER_FAILED(33);         //发卡运营商指令执行失败
		
		private int retCode;
		private RETURN_CODE(int value)
		{
			this.retCode=value;
		}
		
		public int getRetCode()
		{
			return retCode;
		}
	}
	
	/*-- 读卡器中的智能卡状态 --*/
	public enum CA_CARD_STATE
	{
		ST_CA_SC_OUT,         /* 读卡器中没有卡          */
		ST_CA_SC_REMOVING,    /* 正在拔卡，重置状态      */
		ST_CA_SC_INSERTING,   /* 正在插卡，初始化        */
		ST_CA_SC_IN,          /* 读卡器中是可用的卡      */
		ST_CA_SC_ERROR,       /* 读卡器的卡不能识别      */
		ST_CA_SC_UPDATE,      /* 读卡器的卡可升级 */
		ST_CA_SC_UPDATE_ERR,  /* 读卡器的卡升级失败      */
	}

}
