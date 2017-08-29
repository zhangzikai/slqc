--航迹
create table track (
	ydh int(8) not null, 
	name char(32) not null,
	lon double(16, 6) not null,
	lat double(16, 6) not null,
	time datetime);

--调查人员
create table worker (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	name char(16) not null,
	type int(4) not null,
	phone char(16),
	company char(256),
	address char(256),
	notes char(256));

--卡片封面
create table kpfm (
	ztmc char(32) not null, 
	ydh int(8) not null, 
	ydxz char(32),
	ydmj char(32),
	zzb int(8),
	hzb int(8),
	ydjj char(32),
	dxttfh char(32),
	wph char(32),
	dfxzbm int(8),
	lyxzbm int(8),
	shi char(32),
	xian char(32),
	xiang char(32),
	cun char(32),
	xdm char(32),
	lyqyj char(32),
	zrbhq char(32),
	slgy char(32),
	gylc char(32),
	jtlc char(32),
	xd char(32),
	xddw char(256),
	dcrq date);

--引点定位物
create table yangdidww (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	mc char(32),
	bh char(32),
	fwj float(8, 1),
	spj float(8, 2));

--样地定位物
create table yindiandww (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	mc char(32),
	bh char(32),
	fwj float(8, 1),
	spj float(8, 2));

--引线测量
create table yxcl (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	cz char(32),
	fwj float(8, 1),
	qxj float(8, 1),
	xj float(8, 2),
	spj float(8, 2),
	lj float(8, 2));

--周界测量
create table zjcl (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	cz char(32),
	fwj float(8, 1),
	qxj float(8, 1),
	xj float(8, 2),
	spj float(8, 2),
	lj float(8, 2));

--样地因子
create table ydyz (
	ydh int(8) not null,
	ydlb int(4) not null,
	y int(8) not null,
	x int(8) not null,
	gps_y int(8) not null,
	gps_x int(8) not null,
	xian int(8) not null,
	dm int(4) not null,
	hb int(4) not null,
	px int(4) not null,
	pw int(4) not null,
	pd int(4) not null,
	dbxt int(4),
	sqgd float(8, 1),
	fshd int(4),
	qsgmjbl int(4),
	jyll int(4),
	trmc int(4),
	trzd int(4),
	trlshl int(4),
	tchd int(4),
	fzchd int(4),
	kzlyhd int(4),
	zblx int(4),
	gmfgd int(4),
	gmgd float(8, 1),
	cbfgd int(4),
	cbgd float(8, 1),
	zbzfgd int(4),
	dl int(4) not null,
	tdqs int(4),
	lmqs int(4),
	sllb int(4),
	gylsqdj int(4),
	gylbhdj int(4),
	spljydj int(4),
	fycs int(4),
	linzh int(4),
	qy int(4),
	yssz int(4),
	pjnl int(4),
	lingz int(4),
	cq int(4),
	pjxj float(8, 1),
	pjsg float(8, 1),
	ybd float(8, 2),
	slqljg int(4),
	lcjg int(4),
	szjg int(4),
	zrd int(4),
	kjd int(4),
	slzhlx int(4),
	slzhdj int(4),
	sljkdj int(4),
	spszs int(8),
	zzzs int(8),
	trgxdj int(4),
	dlmjdj int(4),
	dlbhyy int(4),
	ywtsdd int(4),
	dcrq date,
	f1 char,
	f2 char,
	f3 char,
	f4 char,
	f5 char);

--跨角林
create table kjl (
	ydh int(8) not null,
	xh int(4) not null,
	mjbl float(8, 2) not null,
	dl int(4) not null,
	tdqs int(4) not null,
	lmqs int(4) not null,
	linzh int(4) not null,
	qy int(4) not null,
	yssz int(4) not null,
	lingz int(4) not null,
	ybd float(8, 2) not null,
	pjsg float(8, 1)not null,
	slqljg int(4) not null,
	szjg int(4) not null,
	spljydj int(4));

--每木检尺
create table mmjc (
	ydh int(8) not null,
	ymh int(4) not null,
	lmlx int(4) not null,
	jclx int(4) not null,
	szmc char(32) not null,
	szdm int(4) not null,
	qqxj float(8, 1),
	bqxj float(8, 1) not null,
	cfgllx int(4) not null,
	lc int(4) not null,
	kjdlxh int(4),
	fwj float(8, 1),
	spj float(8, 1),
	bz char(32),
	ckd int(4),
	szlx int(4),
	jczt int(4));

--树高测量
create table sgcl (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	ymh int(4) not null,
	sz int(4) not null,
	xj float(8, 1),
	sg float(8, 1),
	zxg float(8, 1));

--森林灾害
create table slzh (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	zhlx int(4),
	whbw char(32),
	szymzs int(4),
	szdj int(4));

--植被调查
create table zbdc (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	zblx int(4) not null,
	mc char(32),
	pjgd float(8, 2),
	fgd int(4),
	zs int(4),
	pjdj float(8, 1));

--下木调查
create table xmdc (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	mc char(32) not null,
	gd float(8, 2),
	xj float(8, 1));

--天然更新
create table trgx (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	sz char(32) not null,
	zs1 int(4),
	zs2 int(4),
	zs3 int(4),
	jkzk char(32),
	phqk char(32));

--样地变化
create table ydbh (
	ydh int(8) not null,
	qqdl int(4),
	bqdl int(4),
	dlbhyy char(32),
	qqlinzh int(4),
	bqlinzh int(4),
	linzhbhyy char(32),
	qqqy int(4),
	bqqy int(4),
	qybhyy char(32),
	qqyssz int(4),
	bqyssz int(4),
	ysszbhyy char(32),
	qqlingz int(4),
	bqlingz int(4),
	lingzbhyy char(32),
	qqzblx int(4),
	bqzblx int(4),
	zblxbhyy char(32),
	bz char(256));

--未成林造林地
create table wclzld (
	ydh int(8) not null,
	wclzldqk int(4) not null,
	zlnd int(4),
	ml int(4),
	czmd int(8),
	mmchl int(4),
	gg int(4),
	bz int(4),
	sf int(4),
	fy int(4),
	gh int(4),
	sz char(256),
	szbl char(256));

--其他
create table qt (
	ydh int(8) not null,
	zbfwj float(8, 1),
	cfwj float(8, 1),
	yxjl float(8, 2),
	lc float(8, 1),
	yindian char(256),
	yangdi char(256),
	guding char(256),
	tujing char(256),
	zjcllx int(4),
	jdbhc float(8, 2),
	xdbhc float(8, 2),
	zcwc float(8, 2),
	cfsj time,
	zdsj time,
	jssj time,
	fhsj time,
	gps_type char(32),
	gps_dis int(4),
	gps_begin time,
	gps_end time,
	ydphoto int(4),
	ymphoto int(4),
	yssz char(256),
	yf int(4),
	qq_hzb int(8),
	qq_zzb int(8),
	yindiantu blob,
	yangditu blob,
	yangmutu blob,
	ymtditu blob);

--样木照片，弃用
create table ymzp (
	ydh int(8) not null,
	ymh int(4) not null,
	zph INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	zp blob);

--样地照片，弃用
create table ydzp (
	ydh int(8) not null,
	zph INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	zp blob);
	
--照片
create table zp (
	ydh int(8) not null,
	zph INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	type int(4),
	ymh int(4),
	name char(128),
	notes char(256),
	zp blob);

--调查状态
create table dczt (
	ydh int(8) not null,
	kpfm int(4),
	yindiantu int(4),
	yangditu int(4),
	yxcl int(4),
	zjcl int(4),
	ydyz int(4),
	kjl int(4),
	mmjc int(4),
	ymwzt int(4),
	sgcl int(4),
	slzh int(4),
	zbdc int(4),
	xmdc int(4),
	trgx int(4),
	ydbh int(4),
	wclzld int(4),
	yangdi int(4));

--Info
create table info (
	ydh int(8) not null,
	wgs84_x double(16, 6),
	wgs84_y double(16, 6),
	wgs84_z double(8, 2),
	app_version char(32),
	lib_version char(32),
	device_id char(32),
	export_time datetime,
	lon int(4),
	dx double(16, 6),
	dy double(16, 6),
	dz double(16, 6),
	rx double(16, 6),
	ry double(16, 6),
	rz double(16, 6),
	k double(16, 6));
	
--跨角林树高测量
create table sgcl_kjl (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	kjdlxh int(4) not null,
	ymh int(4) not null,
	sz int(4) not null,
	xj float(8, 1),
	sg float(8, 1),
	zxg float(8, 1));
	
	
	
--样地因子-前期
create table ydyz_old (
	ydh int(8) not null,
	ydlb int(4) not null,
	y int(8) not null,
	x int(8) not null,
	gps_y int(8) not null,
	gps_x int(8) not null,
	xian int(8) not null,
	dm int(4) not null,
	hb int(4) not null,
	px int(4) not null,
	pw int(4) not null,
	pd int(4) not null,
	dbxt int(4),
	sqgd int(4),
	fshd int(4),
	qsgmjbl int(4),
	jyll int(4),
	trmc int(4),
	trzd int(4),
	trlshl int(4),
	tchd int(4),
	fzchd int(4),
	kzlyhd int(4),
	zblx int(4),
	gmfgd int(4),
	gmgd int(4),
	cbfgd int(4),
	cbgd int(4),
	zbzfgd int(4),
	dl int(4) not null,
	tdqs int(4),
	lmqs int(4),
	sllb int(4),
	gylsqdj int(4),
	gylbhdj int(4),
	spljydj int(4),
	fycs int(4),
	linzh int(4),
	qy int(4),
	yssz int(4),
	pjnl int(4),
	lingz int(4),
	cq int(4),
	pjxj int(4),
	pjsg int(4),
	ybd int(4),
	slqljg int(4),
	lcjg int(4),
	szjg int(4),
	zrd int(4),
	kjd int(4),
	slzhlx int(4),
	slzhdj int(4),
	sljkdj int(4),
	spszs int(8),
	zzzs int(8),
	trgxdj int(4),
	dlmjdj int(4),
	dlbhyy int(4),
	ywtsdd int(4),
	dcrq date,
	f1 char,
	f2 char,
	f3 char,
	f4 char,
	f5 char);

--跨角林-前期
create table kjl_old (
	ydh int(8) not null,
	xh int(4) not null,
	mjbl int(4),
	dl int(4) not null,
	tdqs int(4) not null,
	lmqs int(4) not null,
	linzh int(4) not null,
	qy int(4) not null,
	yssz int(4) not null,
	lingz int(4) not null,
	ybd int(4),
	pjsg int(4),
	slqljg int(4) not null,
	szjg int(4) not null,
	spljydj int(4));

--每木检尺-前期
create table mmjc_old (
	ydh int(8) not null,
	ymh int(4) not null,
	lmlx int(4) not null,
	jclx int(4) not null,
	szdm int(4) not null,
	xj int(4),
	cfgllx int(4) not null,
	lc int(4) not null,
	kjdlxh int(4),
	fwj int(4),
	spj int(4));
	
	
--list
create table list (
	zu char(16) not null,
	code int(4) not null,
	name char(256),
	notes char(256));
	
--sz
create table sz (
	name char(32) not null,
	code int(4) not null,
	qglx int(4),
	lzlx int(4),
	zklx int(4),
	cmlx int(4));

--操作日志
create table log_info (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  logtime char(32) not null,
	loginfo char(256) not null);
	
--各种标记
create table tag (
	ydh int(8) not null, 
	f1 int(4),
	f2 int(4),
	f3 int(4),
	f4 int(4),
	f5 int(4),
	f6 int(4),
	f7 int(4),
	f8 int(4),
	f9 int(4),
	f10 int(4),
	f11 int(4),
	f12 int(4),
	f13 int(4),
	f14 int(4),
	f15 int(4),
	f16 char(256),
	f17 char(256),
	f18 char(256),
	f19 char(256),
	f20 char(256),
	f21 char(256),
	f22 char(256),
	f23 char(256),
	f24 char(256),
	f25 char(256),
	f26 char(256),
	f27 char(256),
	f28 char(256),
	f29 char(256),
	f30 char(256)
	);
	
--枯落物调查
create table klwdc (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	yfh char(32),
	hd float(8, 2),
	yfxz float(8, 2),
	yfgz float(8, 2),
	ypxz float(8, 2),
	ypgz float(8, 2));
	
--土壤调查
create table trdc (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	sd int(4),
	rz float(8, 2),
	zd float(8, 2),
	yjt float(8, 2),
	yjtmd float(8, 2),
	tqkxd float(8, 2),
	fyzs float(8, 2),
	yjz float(8, 2),
	sf float(8, 2),
	ph float(8, 2),
	ec float(8, 2),
	TN float(8, 2),
	TP float(8, 2),
	TK float(8, 2),
	AN float(8, 2),
	AP float(8, 2),
	AK float(8, 2),
	Cl float(8, 2),
	B float(8, 2),
	Al float(8, 2),
	Ca float(8, 2),
	Mg float(8, 2),
	Na float(8, 2),
	Fe float(8, 2),
	Mn float(8, 2),
	Zn float(8, 2),
	Cu float(8, 2),
	S float(8, 2),
	Mo float(8, 2),
	Shen float(8, 2),
	Cd float(8, 2),
	Cr float(8, 2),
	Co float(8, 2),
	Pd float(8, 2),
	Hg float(8, 2),
	Ni float(8, 2),
	Se float(8, 2),
	Ag float(8, 2),
	V float(8, 2),
	C6_9 float(8, 2),
	C10_14 float(8, 2),
	C15_28 float(8, 2),
	C29_36 float(8, 2),
	Ben float(8, 2),
	JiaBen float(8, 2),
	YiBen float(8, 2),
	DJ_EJB float(8, 2),
	L_EJB float(8, 2)
  );
	
--具体树种的平均树高
create table sgcl_jtsz (
	ydh int(8) not null,
	xh INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	sz char(32),
	gd float(8, 2));
