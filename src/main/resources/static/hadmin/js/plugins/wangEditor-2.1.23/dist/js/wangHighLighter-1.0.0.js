/* wangHighLighter 
*  1.0.0
* 王福朋
* 2015-01-06
*/
(function (window, $, undefined) {
    if (!$) {
        throw new Error('请检查是否引用了jQuery...');
    }
    if (window.wangHighLighter) {
        return;
    }
    var
        //默认的正则表达式
        basicRegs = {
            rkeyword: /^\s$/, 
            rstr: /(['"])(\1|(.*?[^\\]\1))/g,  // 默认【"..." 或 '...'】 （包含字符和字符串两种类型）
            rreg: /^\s$/,
            rlineComment: /\/\/.*?(?=(<br\/>)|$)/gm,  // 默认【//...】
            rblockComment: /\/\*.*?\*\//gm,  //默认【/*...*/】
            rLabel: /<.*?>/gm  // <> 标签
        },
        //默认的样式（字号、行高、字体）
        basicStyle = {
            fontSize: '14px',
            lineHeight: '20px',
            fontFamily: 'Consolas,Courier New,Inconsolata-g,DejaVu Sans Mono,微软雅黑,宋体',
            keywordBold: true
        };

    /*构造函数
    * rkeywords：关键字正则
    * rstr: 字符串正则
    * rlineComment：行注释正则
    * rblockComment： 块注释正则
    * style：样式（字号、行高、字体）
    * rreg: 正则表达式的正则
    */
    function HL(rkeywords, rstr, rlineComment, rblockComment, style, rreg) {
        this.rkeywords = rkeywords || basicRegs.rkeyword;
        this.rstr = rstr || basicRegs.rstr;
        this.rreg = rreg || basicRegs.rreg;
        this.rlineComment = rlineComment || basicRegs.rlineComment;
        this.rblockComment = rblockComment || basicRegs.rblockComment;
        this.rLabel = basicRegs.rLabel;

        this.wrapLabel = '<br/>';

        this.fontSize = (style && style.fontSize) || basicStyle.fontSize;
        this.lineHeight = (style && style.lineHeight) || basicStyle.lineHeight;
        this.fontFamily = (style && style.fontFamily) || basicStyle.fontFamily;

        var keywordBold = basicStyle.keywordBold;
        if (style && style.keywordBold !== undefined) {
            keywordBold = style.keywordBold;
        }
        this.keywordLabel = !!keywordBold ? 'b' : 'span';
    }
    HL.prototype = {
        constructor: HL,

        keywordsDictionary: [],
        keywordsHL: function (code) {
            var that = this,
                i = 0;
            if (that.rkeywords.test(code)) {
                code = code.replace(that.rkeywords, function (a) {
                    that.keywordsDictionary[i] = a;
                    a = '$keywordIndex{' + i + '}';
                    i++;
                    return '<' + that.keywordLabel + ' style=$keywordStyle$>' + a + '</' + that.keywordLabel + '>';
                });
            }
            return code;
        },

        strDictionary: [],
        strHL: function (code) {
            var that = this,
                i = 0;
            if (that.rstr.test(code)) {
                code = code.replace(that.rstr, function (a) {
                    //去掉标签
                    if (that.rLabel.test(a)) {
                        a = a.replace(that.rLabel, function (a) {
                            if (a === that.wrapLabel) {
                                //考虑多行字符串中间的换行
                                that.strDictionary[i] = a;
                                a = '$strIndx{' + i + '}';;
                                i++;
                                return '</span>' + a + '<span style=$strStyle$>';
                            }
                            return '';
                        });
                    }
                    that.strDictionary[i] = a;
                    a = '$strIndex{' + i + '}';;
                    i++;
                    return '<span style=$strStyle$>' + a + '</span>';
                });
            }
            return code;
        },

        regDictionary: [],
        regHL: function (code) {
            var that = this,
                i = 0;
            if (that.rreg.test(code)) {
                code = code.replace(that.rreg, function (a) {
                    //去掉标签
                    if (that.rLabel.test(a)) {
                        a = a.replace(that.rLabel, '');
                    }
                    that.regDictionary[i] = a;
                    a = '$regIndex{' + i + '}';;
                    i++;
                    return '<span style=$regStyle$>' + a + '</span>';
                });
            }
            return code;
        },

        lineCommentDictionary: [],
        lineCommentHL: function (code) {
            var that = this,
                i = 0;
            if (that.rlineComment.test(code)) {
                code = code.replace(that.rlineComment, function (a) {
                    //去掉标签
                    if (that.rLabel.test(a)) {
                        a = a.replace(that.rLabel, '');
                    }
                    that.lineCommentDictionary[i] = a;
                    a = '$lineCommentIndex{' + i + '}';;
                    i++;
                    return '<span style=$commentStyle$>' + a + '</span>';
                });
            }
            return code;
        },

        blockCommentHL: function (code) {
            var that = this;
            if (that.rblockComment.test(code)) {
                code = code.replace(that.rblockComment, function (a) {
                    //去掉标签
                    if (that.rLabel.test(a)) {
                        a = a.replace(that.rLabel, function (a) {
                            if (a === that.wrapLabel) {
                                //考虑多行注释中间的换行
                                return '</span>' + a + '<span style=$commentStyle$>';
                            }
                            return '';
                        });
                    }
                    return '<span style=$commentStyle$>' + a + '</span>';
                });
            }
            return code;
        },
        codeHL: function (code, theme) {
            var i,
                keywordLength,
                strLength,
                regLength,
                lineCommentLength;

            //转义特殊字符
            code = code.replace(/&/gm, '&amp;')
                       .replace(/</gm, '&lt;')
                       .replace(/>/gm, '&gt;')
                       .replace(/\n/gm, this.wrapLabel)
                       .replace(/\s{1}/gm, '&nbsp;');

            //关键字、字符串、注释
            code = this.keywordsHL(code);
            code = this.strHL(code);
            code = this.regHL(code);
            code = this.lineCommentHL(code);
            code = this.blockCommentHL(code);
            code = code.replace(/\$keywordStyle\$/gm, '"color:' + theme.keywordColor + '"')
                       .replace(/\$strStyle\$/gm, '"color:' + theme.strColor + '"')
                       .replace(/\$regStyle\$/gm, '"color:' + theme.regColor + '"')
                       .replace(/\$commentStyle\$/gm, '"color:' + theme.commentColor + '"');

            //查询字典，还原内容
            for (i = 0, keywordLength = this.keywordsDictionary.length; i < keywordLength; i++) {
                code = code.replace('$keywordIndex{' + i + '}', this.keywordsDictionary[i]);
            }
            for (i = 0, strLength = this.strDictionary.length; i < strLength; i++) {
                code = code.replace('$strIndex{' + i + '}', this.strDictionary[i]);
            }
            for (i = 0, regLength = this.regDictionary.length; i < regLength; i++) {
                code = code.replace('$regIndex{' + i + '}', this.regDictionary[i]);
            }
            for (i = 0, lineCommentLength = this.lineCommentDictionary.length; i < lineCommentLength; i++) {
                code = code.replace('$lineCommentIndex{' + i + '}', this.lineCommentDictionary[i]);
            }
            this.keywordsDictionary = this.strDictionary = this.regDictionary = this.lineCommentDictionary = []; //清空字典

            //其他 自定义高亮
            if (this.otherHL && typeof this.otherHL === 'function') {
                code = this.otherHL(code);
            }

            //-------------------------代码高亮完毕，输出代码表格-------------------------
            var tableTemp = '<table border="0" cellpadding="0" cellspacing="0" width="100%" style="line-height:${0}; font-size:${1};font-family:${2};color:${3};"> ${tbody} </table>',
                tableTemp = tableTemp.replace('${0}', this.lineHeight)
                                     .replace('${1}', this.fontSize)
                                     .replace('${2}', this.fontFamily)
                                     .replace('${3}', theme.color),
                
                trOddTemp = '<tr valign="top" style="background-color:${0};"> ${content} </tr>', //奇数
                trOddTemp = trOddTemp.replace('${0}', theme.oddBgColor),
                trEvenTemp = '<tr valign="top" style="background-color:${0};"> ${content} </tr>', //偶数
                trEvenTemp = trEvenTemp.replace('${0}', theme.evenBgColor),
                tr = '',
                trArray = [],

                tdNumTemp = '<td style="border:0px; border-right-color:${0}; border-right-width:${1}; color:${2}; border-right-style:solid; text-align:right; padding-right:5px; width:40px;"> ${content} </td>',
                tdNumTemp = tdNumTemp.replace('${0}', theme.numBorderColor)
                                     .replace('${1}', theme.numBorderWidth)
                                     .replace('${2}', theme.numColor),
                tdNum = '',

                tdCodeTemp = '<td style="border:0px; padding-left:10px; text-align:left;">${content}</td>',
                tdCode = '',

                //按换行分数组
                lineArray = code.split(this.wrapLabel),
                length = lineArray.length,
                itemForLoop;

            for (i = 0; i < length; i++) {
                itemForLoop = lineArray[i];
                tdCode = tdNum = '';

                tdNum = tdNumTemp.replace('${content}', (i + 1));
                tdCode = tdCodeTemp.replace('${content}', itemForLoop);

                if (i % 2 === 0) {
                    tr = trOddTemp.replace('${content}', tdNum + tdCode);
                } else {
                    tr = trEvenTemp.replace('${content}', tdNum + tdCode);
                }
                trArray[i] = tr;
            }
            return tableTemp.replace('${tbody}', trArray.join(''));
        }
    };

    /* 将关键字字符串，转换为正则表达式
    * keywords: 关键字字符串
    */
    function keywordsToReg(keywords) {
        if (typeof keywords !== 'string') {
            return /^\s$/;
        }
        //去掉前后空格
        keywords = keywords.replace(/^\s+/g, '').replace(/\s+$/g, '');

        //转换表达式
        var rkeywords = new RegExp(keywords.replace(/\s+/g, function () {
            return '\\b|\\b';
        }), 'igm');

        return rkeywords;
    }

    //wangHighLighter 对象
    window.wangHighLighter = {
        langs: {

            actionScript: function (code, theme) {
                var keywords = '-Infinity rest Array as AS3 Boolean break case catch const continue Date decodeURI ' +
					          'decodeURIComponent default delete do dynamic each else encodeURI encodeURIComponent escape ' +
					          'extends false final finally flash_proxy for get if implements import in include Infinity ' +
					          'instanceof int internal is isFinite isNaN isXMLName label namespace NaN native new null ' +
					          'Null Number Object object_proxy override parseFloat parseInt private protected public ' +
					          'return set static String super switch this throw true try typeof uint undefined unescape ' +
					          'use void while with',
                   rkeywords = keywordsToReg(keywords),
                   hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            C: function (code, theme) {
                var keywords = 'auto double int struct break else long switch case enum register typedef char extern return union const float short unsigned continue for signed void default goto sizeof volatile do if while static',
                    rkeywords = keywordsToReg(keywords),
                    hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            'C#': function (code, theme) {
                var keywords = 'var abstract as base bool break byte case catch char checked class const ' +
					          'continue decimal default delegate do double else enum event explicit ' +
					          'extern false finally fixed float for foreach get goto if implicit in int ' +
					          'interface internal is lock long namespace new null object operator out ' +
					          'override params private protected public readonly ref return sbyte sealed set ' +
					          'short sizeof stackalloc static string struct switch this throw true try ' +
					          'typeof uint ulong unchecked unsafe ushort using virtual void while',
                    rkeywords = keywordsToReg(keywords),
                    hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            'C++': function (code, theme) {
                var keywords = 'break case catch class const __finally __exception __try ' +
					          'const_cast continue private public protected __declspec ' +
					          'default delete deprecated dllexport dllimport do dynamic_cast ' +
					          'else enum explicit extern if for friend goto inline ' +
					          'mutable naked namespace new noinline noreturn nothrow ' +
					          'register reinterpret_cast return selectany ' +
					          'sizeof static static_cast struct switch template this ' +
					          'thread throw true false try typedef typeid typename union ' +
					          'using uuid virtual void volatile whcar_t while',
                    rkeywords = keywordsToReg(keywords),
                    hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            css: function (code, theme) {
                var
                   //关键字
                   keywords = 'ascent azimuth background-attachment background-color background-image background-position ' +
					            'background-repeat background baseline bbox border-collapse border-color border-spacing border-style border-top ' +
					            'border-right border-bottom border-left border-top-color border-right-color border-bottom-color border-left-color ' +
					            'border-top-style border-right-style border-bottom-style border-left-style border-top-width border-right-width ' +
					            'border-bottom-width border-left-width border-width border bottom cap-height caption-side centerline clear clip color ' +
					            'content counter-increment counter-reset cue-after cue-before cue cursor definition-src descent direction display ' +
					            'elevation empty-cells float font-size-adjust font-family font-size font-stretch font-style font-variant font-weight font ' +
					            'height left letter-spacing line-height list-style-image list-style-position list-style-type list-style margin-top ' +
					            'margin-right margin-bottom margin-left margin marker-offset marks mathline max-height max-width min-height min-width orphans ' +
					            'outline-color outline-style outline-width outline overflow padding-top padding-right padding-bottom padding-left padding page ' +
					            'page-break-after page-break-before page-break-inside pause pause-after pause-before pitch pitch-range play-during position ' +
					            'quotes right richness size slope src speak-header speak-numeral speak-punctuation speak speech-rate stemh stemv stress ' +
					            'table-layout text-align top text-decoration text-indent text-shadow text-transform unicode-bidi unicode-range units-per-em ' +
					            'vertical-align visibility voice-family volume white-space widows width widths word-spacing x-height z-index',
                   rkeywords = keywordsToReg(keywords),
                   //css没有字符串
                   rstr = /^\s$/,
                   //css没有行注释
                   rlineComment = /^\s$/;
                hl = new HL(rkeywords, rstr, rlineComment, undefined, { keywordBold: false });
                return hl.codeHL(code, theme);
            },

            delphi: function (code, theme) {
                var
                   //关键字
                   keywords = 'abs addr and ansichar ansistring array as asm begin boolean byte cardinal ' +
					            'case char class comp const constructor currency destructor div do double ' +
					            'downto else end except exports extended false file finalization finally ' +
					            'for function goto if implementation in inherited int64 initialization ' +
					            'integer interface is label library longint longword mod nil not object ' +
					            'of on or packed pansichar pansistring pchar pcurrency pdatetime pextended ' +
					            'pint64 pointer private procedure program property pshortstring pstring ' +
					            'pvariant pwidechar pwidestring protected public published raise real real48 ' +
					            'record repeat set shl shortint shortstring shr single smallint string then ' +
					            'threadvar to true try type unit until uses val var varirnt while widechar ' +
					            'widestring with word write writeln xor',
                   rkeywords = keywordsToReg(keywords),
                   //块注释
                   rblockComment = /(\(\*.*?\*\))|({.*?})/gm;
                hl = new HL(rkeywords, undefined, undefined, rblockComment);
                return hl.codeHL(code, theme);
            },

            'F#': function (code, theme) {
                var keywords = 'abstract lsl and lsr as lxor assert match member asr mod' +
                                'begin module class mutable namespace default new delegate null' +
                                'do of done open downcast or downto override' +
                                'else rec end sig exception static false  struct' +
                                'finally then for to fun true function try' +
                                'if type in val inherit when inline upcast' +
                                'interface while land  with lor async method ' +
                                'atomic mixin break namespace checked object component process ' +
                                'const property constraint  protected constructor public continue pure ' +
                                'decimal readonly eager return enum sealed event switch ' +
                                'external virtual fixed void functor volatile include where',
                    rkeywords = keywordsToReg(keywords),
                    hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            Go: function (code, theme) {
                var keywords = 'break default func interface select case defer go map struct chan else goto package switch const fallthrough if range type continue for import return var',
                    rkeywords = keywordsToReg(keywords),
                    hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            html:function (code,theme) {
                var
                    //关键字
                    rkeywords = /&lt;(?!!--)\/?((html)|(head)|(body)|(style)|(script)).*?&gt;/igm,
                    rstr = /^\s$/;
                    //没有行注释
                    rlineComment = /^\s$/,
                    //块注释
                    rblockComment = /&lt;!--.*?--&gt;/gm,
                    hl = new HL(rkeywords, rstr, rlineComment, rblockComment, { keywordBold: false });

                return hl.codeHL(code, theme);
            },

            java: function (code, theme) {
                var
                   //关键字
                   keywords = 'abstract assert boolean break byte case catch char class const ' +
					            'continue default do double else enum extends ' +
					            'false final finally float for goto if implements import ' +
					            'instanceof int interface long native new null ' +
					            'package private protected public return ' +
					            'short static strictfp super switch synchronized this throw throws true ' +
					            'transient try void volatile while',
                   rkeywords = keywordsToReg(keywords),
                   hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            javascript: function (code, theme) {
                var
                   //关键字
                   keywords = 'break case catch  continue ' +
                              'default delete do else false  ' +
                              'for function if in instanceof ' +
                              'new null return super switch ' +
                              'this throw true try typeof var while with regexp',
                   rkeywords = keywordsToReg(keywords),
                   //rreg = /\/(?!(\/)|(>)).*[^\\]\/\w*?(?=[,;\)\.](<br\/>)|$)/gm,
                   rreg = /^\s$/,
                   hl = new HL(rkeywords, undefined, undefined, undefined, undefined, rreg);
                return hl.codeHL(code, theme);
            },

            'objective-C': function (code, theme) {
                var
                   //关键字
                   keywords = 'int long float double char void bool  NSNumber NSString NSMutableString NSMutableArray NSArray NSMutableSet NSSet NSMutableDictionary NSDictionary for in return while do break continue if else NSObject NSInteger',
                   rkeywords = keywordsToReg(keywords),
                   hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            pascal: function (code, theme) {
                var
                   //关键字
                   keywords = 'absolute abstract and array as asm assembler at automated  begin case  cdecl class const constructor contains default destructor dispid dispinterface div  do downto dynamic else end except export exports external far file finalization finally for forward function goto if implementation implements in index inherited initialization  inline interface  is label  library message  mod name near nil nodefault not object of on or out overload override package packed  pascal private  procedure program property protected public  published raise  read   readonly record   register   reintroduce repeat   requires resident   resourcestring  safecall set   shl    shr  stdcall    stored  string  then  threadvar to  try    type  unit  until  uses  var    virtual   while  with  write   writeonly',
                   rkeywords = keywordsToReg(keywords),
                   //块注释
                   rblockComment = /(\(\*.*?\*\))|({.*?})/gm,
                   hl = new HL(rkeywords, undefined, undefined, rblockComment);
                return hl.codeHL(code, theme);
            },

            perl: function (code, theme) {
                var
                   //关键字
                   keywords = 'bless caller continue dbmclose dbmopen die do dump else elsif eval exit ' +
		                        'for foreach goto if import last local my next no our package redo ref ' +
		                        'require return sub tie tied unless untie until use wantarray while',
                   rkeywords = keywordsToReg(keywords),
                   //行注释
                   rlineComment = /#.*?(?=(<br\/>)|$)/gm,
                   //块注释
                   rblockComment = /=.+?=cut/gm,
                   hl = new HL(rkeywords, undefined, rlineComment, rblockComment);
                return hl.codeHL(code, theme);
            },

            php: function (code, theme) {
                var
                   //关键字
                   keywords = 'and or xor array as break case ' +
					            'cfunction class const continue declare default die do else ' +
					            'elseif enddeclare endfor endforeach endif endswitch endwhile ' +
					            'extends for foreach function include include_once global if ' +
					            'new old_function return static switch use require require_once ' +
					            'var while abstract interface public implements extends private protected throw',
                   rkeywords = keywordsToReg(keywords),
                hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            powershell: function (code, theme) {
                var
                   //关键字
                   keywords = 'Add-Content Add-History Add-Member Add-PSSnapin Clear Clear-Item ' +
                                'Clear-ItemProperty Clear-Variable Compare-Object ConvertFrom-SecureString Convert-Path ' +
				                'ConvertTo-Html ConvertTo-SecureString Copy Copy-ItemProperty Export-Alias ' +
				                'Export-Clixml Export-Console Export-Csv ForEach Format-Custom Format-List ' +
				                'Format-Table Format-Wide Get-Acl Get-Alias Get-AuthenticodeSignature Get-ChildItem Get-Command ' +
				                'Get-Content Get-Credential Get-Culture Get-Date Get-EventLog Get-ExecutionPolicy ' +
				                'Get-Help Get-History Get-Host Get-Item Get-ItemProperty Get-Location Get-Member ' +
				                'Get-PfxCertificate Get-Process Get-PSDrive Get-PSProvider Get-PSSnapin Get-Service ' +
				                'Get-TraceSource Get-UICulture Get-Unique Get-Variable Get-WmiObject Group-Object ' +
				                'Import-Alias Import-Clixml Import-Csv Invoke-Expression Invoke-History Invoke-Item ' +
				                'Join-Path Measure-Command Measure-Object Move Move-ItemProperty New-Alias ' +
				                'New-Item New-ItemProperty New-Object New-PSDrive New-Service New-TimeSpan ' +
				                'New-Variable Out-Default Out-File Out-Host Out-Null Out-Printer Out-String Pop-Location ' +
				                'Push-Location Read-Host Remove-Item Remove-ItemProperty Remove-PSDrive Remove-PSSnapin ' +
				                'Remove-Variable Rename-Item Rename-ItemProperty Resolve-Path Restart-Service Resume-Service ' +
				                'Select-Object Select-String Set-Acl Set-Alias Set-AuthenticodeSignature Set-Content ' +
				                'Set-Date Set-ExecutionPolicy Set-Item Set-ItemProperty Set-Location Set-PSDebug ' +
				                'Set-Service Set-TraceSource Set Sort-Object Split-Path Start-Service ' +
				                'Start-Sleep Start-Transcript Stop-Process Stop-Service Stop-Transcript Suspend-Service ' +
				                'Tee-Object Test-Path Trace-Command Update-FormatData Update-TypeData Where ' +
				                'Write-Debug Write-Error Write Write-Output Write-Progress Write-Verbose Write-Warning',
                   rkeywords = keywordsToReg(keywords),
                   //行注释
                   rlineComment = /#.*?(?=(<br\/>)|$)/gm,
                   //块注释
                   rblockComment = /&lt;(<.+>)?#.*?#&gt;/gm;
                hl = new HL(rkeywords, undefined, rlineComment, rblockComment);
                return hl.codeHL(code, theme);
            },

            python: function (code, theme) {
                var
                   //关键字
                   keywords = 'and assert break class continue def del elif else ' +
					            'except exec finally for from global if import in is ' +
					            'lambda not or pass print raise return try yield while',
                   rkeywords = keywordsToReg(keywords),
                   //字符串
                   rstr = /(['"(''')(""")])(\1|(.*?[^\\]\1))/gm,
                   //行注释
                   rlineComment = /#.*?(?=(<br\/>)|$)/gm,
                   //python没有块注释
                   rblockComment = /^\s$/,
                   hl = new HL(rkeywords, rstr, rlineComment, rblockComment);
                return hl.codeHL(code, theme);
            },

            ruby: function (code, theme) {
                var
                   //关键字
                   keywords = 'alias and BEGIN begin break case class def define_method defined do each else elsif ' +
					            'END end ensure false for if in module new next nil not or raise redo rescue retry return ' +
					            'self super then throw true undef unless until when while yield',
                   rkeywords = keywordsToReg(keywords),
                   //行注释
                   rlineComment = /#.*?(?=(<br\/>)|$)/gm,
                   //块注释
                   rblockComment = /=(<.+?>)?begin(<\/.+?>)?(&nbsp;)+?.*?=(<.+?>)?end(<\/.+?>)?/gm,
                   hl = new HL(rkeywords, undefined, rlineComment, rblockComment);
                return hl.codeHL(code, theme);
            },

            SQL: function (code, theme) {
                var
                   //关键字
                   keywords = 'absolute action add after alter as asc at authorization begin bigint ' +
					            'binary bit by cascade char character check checkpoint close collate ' +
					            'column commit committed connect connection constraint contains continue ' +
					            'create cube current current_date current_time cursor database date ' +
					            'deallocate dec decimal declare default delete desc distinct double drop ' +
					            'dynamic else end end-exec escape except exec execute false fetch first ' +
					            'float for force foreign forward free from full function global goto grant ' +
					            'group grouping having hour ignore index inner insensitive insert instead ' +
					            'int integer intersect into is isolation key last level load local max min ' +
					            'minute modify move name national nchar next no numeric of off on only ' +
					            'open option order out output partial password precision prepare primary ' +
					            'prior privileges procedure public read real references relative repeatable ' +
					            'restrict return returns revoke rollback rollup rows rule schema scroll ' +
					            'second section select sequence serializable set size smallint static ' +
					            'statistics table temp temporary then time timestamp to top transaction ' +
					            'translation trigger true truncate uncommitted union unique update values ' +
					            'varchar varying view when where with work',
                   rkeywords = keywordsToReg(keywords),
                   //行注释
                   rlineComment = /-.*?(?=(<br\/>)|$)/gm,
                   hl = new HL(rkeywords, undefined, rlineComment);
                return hl.codeHL(code, theme);
            },

            swift: function (code, theme) {
                var
                   //关键字
                   keywords = 'class deinit enum extension func import init let protocol static struct subscript typealias var ' +
                                ' break case continue default do else fallthrough if in for return switch where while ' +
                                ' as dynamicType is new super self Self Type __COLUMN__ __FILE__ __FUNCTION__ __LINE__ ' +
                                ' associativity didSet get infix inout left mutating none nonmutating operator override postfix precedence prefix rightset unowned unowned unowned weak willSet',
                   rkeywords = keywordsToReg(keywords),
                   hl = new HL(rkeywords);
                return hl.codeHL(code, theme);
            },

            vb: function (code, theme) {
                var
                   //关键字
                   keywords = 'AddHandler AddressOf AndAlso Alias And Ansi As Assembly Auto ' +
					            'Boolean ByRef Byte ByVal Call Case Catch CBool CByte CChar CDate ' +
					            'CDec CDbl Char CInt Class CLng CObj Const CShort CSng CStr CType ' +
					            'Date Decimal Declare Default Delegate Dim DirectCast Do Double Each ' +
					            'Else ElseIf End Enum Erase Error Event Exit False Finally For Friend ' +
					            'Function Get GetType GoSub GoTo Handles If Implements Imports In ' +
					            'Inherits Integer Interface Is Let Lib Like Long Loop Me Mod Module ' +
					            'MustInherit MustOverride MyBase MyClass Namespace New Next Not Nothing ' +
					            'NotInheritable NotOverridable Object On Option Optional Or OrElse ' +
					            'Overloads Overridable Overrides ParamArray Preserve Private Property ' +
					            'Protected Public RaiseEvent ReadOnly ReDim REM RemoveHandler Resume ' +
					            'Return Select Set Shadows Shared Short Single Static Step Stop String ' +
					            'Structure Sub SyncLock Then Throw To True Try TypeOf Unicode Until ' +
					            'Variant When While With WithEvents WriteOnly Xor',
                   rkeywords = keywordsToReg(keywords),
                   //行注释
                   rlineComment = /'.*?(?=(<br\/>)|$)/gm,
                   //vb没有块注释
                   rblockComment = /^\s$/,
                   hl = new HL(rkeywords, null, rlineComment, rblockComment);
                return hl.codeHL(code, theme);
            },

            xml: function (code, theme) {
                var
                    //关键字
                    rkeywords = /^\s$/,
                    //没有行注释
                    rlineComment = /^\s$/,
                    //块注释
                    rblockComment = /&lt;!--.*?--&gt;/gm,
                    hl = new HL(rkeywords, undefined, rlineComment, rblockComment);
                return hl.codeHL(code, theme);
            }
        },
        themes: {
            simple: {
                evenBgColor: '#ffffff',  //偶数行背景色
                oddBgColor: '#f1f1f1',  //奇数行背景色
                numColor: '#c7c7c7',  //行号颜色
                numBorderColor: '#6ce26c',  //行号边框颜色
                numBorderWidth: '3px',  //行号边框宽度
                color: '#000000',  //常规颜色
                keywordColor: '#1a76a3',  //关键字颜色
                strColor: '#0000ff',  //字符串颜色
                regColor: '#0000ff',  //正则颜色
                commentColor: '#008200'  //注释颜色
            },
            'visual Studio': {
                evenBgColor: '#ffffff',  //偶数行背景色
                oddBgColor: '#ffffff',  //奇数行背景色
                numColor: '#2b91af',  //行号颜色
                numBorderColor: '#a5a5a5',  //行号边框颜色
                numBorderWidth: '1px',  //行号边框宽度
                color: '#000000',  //常规颜色
                keywordColor: '#0000ff',  //关键字颜色
                strColor: '#a31515',  //字符串颜色
                regColor: '#a31515',  //正则颜色
                commentColor: '#008000'  //注释颜色
            },
            eclipse: {
                evenBgColor: '#ffffff',  //偶数行背景色
                oddBgColor: '#ffffff',  //奇数行背景色
                numColor: '#848484',  //行号颜色
                numBorderColor: '#d4d0c8',  //行号边框颜色
                numBorderWidth: '1px',  //行号边框宽度
                color: '#000000',  //常规颜色
                keywordColor: '#7f0055',  //关键字颜色
                strColor: '#2a00ff',  //字符串颜色
                regColor: '#2a00ff',  //正则颜色
                commentColor: '#647ecb'  //注释颜色
            },
            sublime: {
                evenBgColor: '#272822',  //偶数行背景色
                oddBgColor: '#272822',  //奇数行背景色
                numColor: '#8f908a',  //行号颜色
                numBorderColor: '#464741',  //行号边框颜色
                numBorderWidth: '1px',  //行号边框宽度
                color: '#f8f8f2',  //常规颜色
                keywordColor: '#e92744',  //关键字颜色
                strColor: '#ae81ff',  //字符串颜色
                regColor: '#ae81ff',  //正则颜色
                commentColor: '#75715e'  //注释颜色
            },
            django: {
                evenBgColor: '#0a2b1d',  //偶数行背景色
                oddBgColor: '#0b2f20',  //奇数行背景色
                numColor: '#437252',  //行号颜色
                numBorderColor: '#41a83e',  //行号边框颜色
                numBorderWidth: '3px',  //行号边框宽度
                color: '#f8f8f8',  //常规颜色
                keywordColor: '#96dd3b',  //关键字颜色
                strColor: '#9df39f',  //字符串颜色
                regColor: '#9df39f',  //正则颜色
                commentColor: '#336442'  //注释颜色
            },
            emacs: {
                evenBgColor: '#0f0f0f',  //偶数行背景色
                oddBgColor: '#000000',  //奇数行背景色
                numColor: '#bdbdbd',  //行号颜色
                numBorderColor: '#990000',  //行号边框颜色
                numBorderWidth: '3px',  //行号边框宽度
                color: '#d3d3d3',  //常规颜色
                keywordColor: '#01e7e7',  //关键字颜色
                strColor: '#ff9e7b',  //字符串颜色
                regColor: '#ff9e7b',  //正则颜色
                commentColor: '#ff7d27'  //注释颜色
            },
            fadeToGrey: {
                evenBgColor: '#000000',  //偶数行背景色
                oddBgColor: '#121212',  //奇数行背景色
                numColor: '#c3c3c3',  //行号颜色
                numBorderColor: '#3185b9',  //行号边框颜色
                numBorderWidth: '3px',  //行号边框宽度
                color: '#dddddd',  //常规颜色
                keywordColor: '#d01d33',  //关键字颜色
                strColor: '#e3e658',  //字符串颜色
                regColor: '#e3e658',  //正则颜色
                commentColor: '#696854'  //注释颜色
            },
            midNight: {
                evenBgColor: '#0f192a',  //偶数行背景色
                oddBgColor: '#0f192a',  //奇数行背景色
                numColor: '#345067',  //行号颜色
                numBorderColor: '#435a5f',  //行号边框颜色
                numBorderWidth: '3px',  //行号边框宽度
                color: '#d1edff',  //常规颜色
                keywordColor: '#b43d3d',  //关键字颜色
                strColor: '#1dc116',  //字符串颜色
                regColor: '#1dc116',  //正则颜色
                commentColor: '#428bdd'  //注释颜色
            },
            rDark: {
                evenBgColor: '#1b2426',  //偶数行背景色
                oddBgColor: '#1b2426',  //奇数行背景色
                numColor: '#b9bdb6',  //行号颜色
                numBorderColor: '#435a5f',  //行号边框颜色
                numBorderWidth: '3px',  //行号边框宽度
                color: '#b9bdb6',  //常规颜色
                keywordColor: '#5ba1cf',  //关键字颜色
                strColor: '#5ce638',  //字符串颜色
                regColor: '#5ce638',  //正则颜色
                commentColor: '#878a85'  //注释颜色
            }
        },
        /*（接口）代码高亮
        * lang: 语言
        * theme: 主题
        * code: 代码
        */
        highLight: function (lang, theme, code) {
            lang = typeof lang === 'string' ? window.wangHighLighter.langs[lang] : undefined;
            if (!lang) {
                return;
            }
            theme = typeof theme === 'string' ? window.wangHighLighter.themes[theme] : undefined;
            if (!theme) {
                return;
            }
            return lang(code, theme);
        },
        /*（接口）获取语言数组
        */
        getLangArray: function () {
            var arr = [],
                item;
            for (item in wangHighLighter.langs) {
                arr.push(item);
            }
            return arr;
        },
        /*（接口）获取主题数组
        */
        getThemeArray: function () {
            var arr = [],
                item;
            for (item in wangHighLighter.themes) {
                arr.push(item);
            }
            return arr;
        }
    };

})(window, window.jQuery);