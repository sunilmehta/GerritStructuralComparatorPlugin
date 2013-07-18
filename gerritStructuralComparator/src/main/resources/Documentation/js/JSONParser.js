var multiArray = "";

var lastClickedUrl = "";

var changes = 0;

var counter = 0;

var modifiedMethod = [];

var baseVersion = "", patchVersion = "";

var baseChangeArray = new Array();

var patchChangeArray = new Array();

function getGerritCommitDetails(){
	var xmlhttp;
	
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else { 
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var jsonData = xmlhttp.responseText;
			var parsedJSON = JSON.parse(xmlhttp.responseText).changeIDs;
			createStatusTable( parsedJSON.reverse() );
		}
	}
	xmlhttp.open("GET", "../gerritPlugin", false);
	xmlhttp.send();
}


function createStatusTable(commitList){
	var tbody = "<tbody>";
	for ( var i = 0; i < commitList.length; i++) {
		tbody +="\n<tr><td><a onclick=\"init(this)\">" + commitList[i].change_id + " </td><td> " + commitList[i].commitMsg + "</td></tr>";
	}
	tbody += "</tbody>";
	document.getElementById("openStatus").innerHTML = tbody;
}

// This Method compares selected Patches with BaseVersion
function PatchComparison(patchSetUrl) {
	document.getElementById("containerId").style.display = "none";
	document.getElementById("wait").style.display = "block";
	
	if( patchSetUrl == '' ){
		if( lastClickedUrl != null ){
			var urlPath = lastClickedUrl.split(",");
			if( urlPath.length == 3 ){
				patchSetUrl = urlPath[0] + ",1," + urlPath[2];
			}
			patchSetUrl = patchSetUrl.substring(0,patchSetUrl.length-1 ).concat('1');
		}
	}
	
	if (patchSetUrl == lastClickedUrl) {
		baseVersion = "No Difference Found";
		patchVersion = "";
		diffUsingJS(baseVersion, patchVersion, null);
		document.getElementById("containerId").style.display = "block";
		document.getElementById("wait").style.display = "none";
	} else {
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		} else {
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				var comparatorResult = null;

				comparatorResult = xmlhttp.responseText;
				
				if ( comparatorResult.indexOf("JavaCode") === 0 ) {
					$('#ModificationDetails').treetable('destroy');
					document.getElementById("ModificationDetails").innerHTML = '';
					baseVersion = "";
					patchVersion = comparatorResult.replace('JavaCode', '');
					diffUsingJS(baseVersion, patchVersion, null);
				} else {
					parseJSONResponse(comparatorResult);
				}
			}
			document.getElementById("containerId").style.display = "block";
			document.getElementById("wait").style.display = "none";
		}
		xmlhttp.open("GET", "../gerritPlugin?patchSetURL1=" + lastClickedUrl
				+ "&patchSetURL2=" + patchSetUrl, true);
		xmlhttp.send();
	}
	lastClickedUrl = patchSetUrl;
}

// This Method fetches the data from the provided URL
function fetchDatafromURL(url, patchNo) {
	var comparatorResult = "";
	var xmlhttp;

	document.getElementById("containerId").style.display = "none";
	document.getElementById("wait").style.display = "block";
	
	lastClickedUrl = url;

	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			comparatorResult = xmlhttp.responseText;
			
			var patchSetLabel = "<a onclick=\"PatchComparison('')\">Base </a>";
			var baseSetLabel = "<a onclick=\"PatchComparison('')\">Base </a>";
			for ( var i = 0; i < multiArray.length; i++) {
				patchSetLabel += " ";
				patchSetLabel += multiArray[i][patchNo];
			}

			document.getElementById("patchSetLabel1").innerHTML = patchSetLabel;
			document.getElementById("patchSetLabel2").innerHTML = patchSetLabel;

			if ( comparatorResult.indexOf("JavaCode") === 0 ) {
				$('#ModificationDetails').treetable('destroy');
				document.getElementById("ModificationDetails").innerHTML = '';
				baseVersion = "";
				patchVersion = comparatorResult.replace('JavaCode', '');
				diffUsingJS(baseVersion, patchVersion, null);
			} else {
				parseJSONResponse(comparatorResult);
			}
			document.getElementById("wait").style.display = "none";
			document.getElementById("containerId").style.display = "block";
			document.getElementById("HideDetail").style.display = "none";
			document.getElementById("ShowDetail").style.display = "block";
		}
		
	}
	xmlhttp.open("GET", "../gerritPlugin?url=" + url, true);
	xmlhttp.send();
}


// This Method parses the JSONResponse
function parseJSONResponse(comparatorResult) {
	var parsedJSON = JSON.parse(comparatorResult);
	counter = -1;
	changes = 0;
	baseVersion = "";
	patchVersion = "";
	modifiedMethod = [];
	var changedFileTreeStructure = "<table id=\"ModificationDetails\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr data-tt-id='1'><td><span class='compilationUnit'>Compilation Unit </span></td></tr> <tr data-tt-id='1-1' data-tt-parent-id='1'><td><span class='javaIcon'>";
	
	var draftMsgArray = parsedJSON.draftMessage;
	// package name changes
	if (parsedJSON.pkg.diff == -1 || parsedJSON.pkg.diff == 10
			|| parsedJSON.pkg.diff == 1) {
		baseVersion += parsedJSON.pkg.lines[0].value;
		patchVersion += parsedJSON.pkg.lines[1].value;
	}

	// changes in Import statement
	for ( var i = 0; i < parsedJSON.imports.length; i++) {
		if (parsedJSON.imports[i].diff == 1) {
			baseVersion += '\n';
			baseVersion += parsedJSON.imports[i].lines[0].value;
			patchVersion += '\n';
		} else if (parsedJSON.imports[i].diff == -1) {
			baseVersion += '\n';
			patchVersion += '\n';
			patchVersion += parsedJSON.imports[i].lines[1].value;
		} else if (parsedJSON.imports[i].diff == 10) {
			baseVersion += '\n';
			baseVersion += parsedJSON.imports[i].lines[0].value;
			patchVersion += '\n';
			patchVersion += parsedJSON.imports[i].lines[1].value;
		}
	}

	// Changes in program body
	for ( var i = 0; i < parsedJSON.types.length; i++) {
		if (parsedJSON.types[i].diff == 1) {
			baseVersion += '\n';
			baseVersion += parsedJSON.types[i].declarations[0].completeNodeValue;
			patchVersion += '\n';
			changedFileTreeStructure += parsedJSON.types[i].declarations[0].name
					+ "</span></td></tr>";
			
		} else if (parsedJSON.types[i].diff == -1) {
			baseVersion += '\n';
			patchVersion += '\n';
			patchVersion += parsedJSON.types[i].declarations[1].completeNodeValue;
			changedFileTreeStructure += parsedJSON.types[i].declarations[1].name
					+ "</span></td></tr>";
			
		} else if (parsedJSON.types[i].diff == 10) {
			changedFileTreeStructure += parsedJSON.types[i].declarations[0].name
					+ "</span></td></tr>";

			for ( var k = 0; k < parsedJSON.types[i].commonChilds.length; k++) {
				if (parsedJSON.types[i].commonChilds[k].diff == 10) {
					var key = null;
					if( parsedJSON.types[i].commonChilds[k].declarations[0].hasOwnProperty('parameters') ){
						var parameter =  parsedJSON.types[i].commonChilds[k].declarations[0].parameters;
						parameter  = parameter.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
						key = parsedJSON.types[i].commonChilds[k].declarations[0].name + '( ' + parameter + ')';
						changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1)
									+ "' data-tt-parent-id='1-1'><td><span class=\"fileModified\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[0].name + '( ' + parameter + ')'
									+ "</a></span></td></tr>"
						
					}else{
						key = parsedJSON.types[i].commonChilds[k].declarations[0].name;
						changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1)
								+ "' data-tt-parent-id='1-1'><td><span class=\"fileModified\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[0].name
								+ "</a></span></td></tr>"
					}
					//Selecting Modified method start
					baseVersion += '\n';
			        baseVersion += parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        patchVersion += '\n';
			        patchVersion += parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
			      //Selecting Modified method End
			        
			        baseChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        patchChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
			        
					var commonChilds = parsedJSON.types[i].commonChilds[k].commonChilds;
					for ( var x = 0; x < commonChilds.length; x++) {
						if (commonChilds[x].diff == 1) {

						} else if (commonChilds[x].diff == -1) {

						}
					}
				} else if (parsedJSON.types[i].commonChilds[k].diff == -1) {
					var key = null;
					if( parsedJSON.types[i].commonChilds[k].declarations[1].hasOwnProperty('parameters') ){
						var parameter =  parsedJSON.types[i].commonChilds[k].declarations[1].parameters;
						parameter  = parameter.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
						key = parsedJSON.types[i].commonChilds[k].declarations[1].name  + '( ' + parameter + ')';
						changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1)
								+ "' data-tt-parent-id='1-1'><td><span class=\"fileNew\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[1].name  + '( ' + parameter + ')'
								+ "</a></span></td></tr>"
					}else{
						key = parsedJSON.types[i].commonChilds[k].declarations[1].name;
						changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1)
								+ "' data-tt-parent-id='1-1'><td><span class=\"fileNew\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[1].name
								+ "</a></span></td></tr>"
					}
					
					baseVersion += '\n';
					patchVersion += '\n';
			        patchVersion += parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
			        
			        baseChangeArray[key] = "";
			        patchChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
				} else if (parsedJSON.types[i].commonChilds[k].diff == 1) {
					var key = null;
					if( parsedJSON.types[i].commonChilds[k].declarations[0].hasOwnProperty('parameters') ){
						var parameter =  parsedJSON.types[i].commonChilds[k].declarations[0].parameters;
						parameter  = parameter.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
						key = parsedJSON.types[i].commonChilds[k].declarations[0].name + '(' + parameter +')';
						changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1)
								+ "' data-tt-parent-id='1-1'><td><span class=\"fileDelete\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[0].name + '(' + parameter +')'
								+ "</a></span></td></tr>"
					}else{
						key = parsedJSON.types[i].commonChilds[k].declarations[0].name;
						changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1)
								+ "' data-tt-parent-id='1-1'><td><span class=\"fileDelete\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[0].name
								+ "</a></span></td></tr>"
					}
					
					baseVersion += '\n';
					patchVersion += '\n';
			        baseVersion += parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        
			        baseChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        patchChangeArray[key] = "";
				}
			}
		}
	}

	changedFileTreeStructure += "</tbody></table>"
	document.getElementById("ModificationDetails").innerHTML = changedFileTreeStructure;

	if (baseVersion == "" && patchVersion == "") {
		$('#ModificationDetails').treetable('destroy');
		document.getElementById("ModificationDetails").innerHTML = '';
		baseVersion = "No changes Found";
		patchVersion = "No changes Found";
	}
	diffUsingJS(baseVersion, patchVersion, draftMsgArray);
	$(".hide_Rows").hide();
	$('#ModificationDetails').treetable('destroy');
	createClassStructure('#ModificationDetails');
}

function diffUsingJS(baseVersion, patchVersion, draftMsgArray) {
	var $ = dojo.byId;
	var url = window.location.toString().split("#")[0];
	var base = difflib.stringAsLines(baseVersion);
	var newtxt = difflib.stringAsLines(patchVersion);
	var sm = new difflib.SequenceMatcher(base, newtxt);
	var opcodes = sm.get_opcodes();
	var diffoutputdiv = $("diffoutput");
	while (diffoutputdiv.firstChild)
		diffoutputdiv.removeChild(diffoutputdiv.firstChild);
	contextSize = null;
	diffoutputdiv.appendChild(diffview.buildView({
		baseTextLines : base,
		newTextLines : newtxt,
		opcodes : opcodes,
		baseTextName : "Base Set",
		newTextName : "Patch Set",
		contextSize : contextSize,
		viewType : 0,
		draftMsgArray : draftMsgArray
	}));
	for ( var i = 0; i < opcodes.length; i++) {
		code = opcodes[i];
		if (code[0] != "equal") {
			changes++;
		}
	}
}

var row = 'Marker0';
$(document).jkey('f6', function() {
	var marker = '';
	if (counter < changes) {
		counter++;
		if (counter == changes) {
			counter = 0;
		}
		marker = '#Marker' + counter;
	}
	row = 'Marker' + counter;
	var _offset = $(marker).offset();
	var _topoffset = _offset.top;
	var className = document.getElementById('diffoutput').className;
	if( className == "vScroll" ){
		var w = $(".vScroll");
		$(".vScroll").scrollTop(_topoffset - w.height() / 2);
	}else{
		var w = $(".vScrollFullScreen");
		$(".vScrollFullScreen").scrollTop(_topoffset - w.height() / 2);
	}
});

$(document).jkey('f7', function() {
	var marker = '';
	if (counter >= 0) {
		counter--;
		if (counter == -1) {
			counter = changes - 1;
		}
		marker = '#Marker' + counter;
	}
	row = 'Marker' + counter;
	var _offset = $(marker).offset();
	var _topoffset = _offset.top;
	var className = document.getElementById('diffoutput').className;
	if( className == "vScroll" ){
		var w = $(".vScroll");
		$(".vScroll").scrollTop(_topoffset - w.height() / 2);
	}else{
		var w = $(".vScrollFullScreen");
		$(".vScrollFullScreen").scrollTop(_topoffset - w.height() / 2);
	}

});

$(document).ready(function() {
	
	$(".hide_Rows").hide();
	
	$(".showDetails").click(function() {
    	$(".hide_Rows").toggle();
    	$(".showDetails").toggle();
    });
	
	counter = -1;
	$("#markerNext").click(function() {
		var marker = '';
		if (counter < changes) {
			counter++;
			if (counter == changes) {
				counter = 0;
			}
			marker = '#Marker' + counter;
		}
		row = 'Marker' + counter;
		var _offset = $(marker).offset();
		var _topoffset = _offset.top;
		var className = document.getElementById('diffoutput').className;
		if( className == "vScroll" ){
			var w = $(".vScroll");
			$(".vScroll").scrollTop(_topoffset - w.height() / 2);
		}else{
			var w = $(".vScrollFullScreen");
			$(".vScrollFullScreen").scrollTop(_topoffset - w.height() / 2);
		}
		
	});

	$("#markerPrev").click(function() {
		var marker = '';
		if (counter >= 0) {
			counter--;
			if (counter == -1) {
				counter = changes - 1;
			}
			marker = '#Marker' + counter;
		}
		row = 'Marker' + counter;
		var _offset = $(marker).offset();
		var _topoffset = _offset.top;
		var className = document.getElementById('diffoutput').className;
		if( className == "vScroll" ){
			var w = $(".vScroll");
			$(".vScroll").scrollTop(_topoffset - w.height() / 2);
		}else{
			var w = $(".vScrollFullScreen");
			$(".vScrollFullScreen").scrollTop(_topoffset - w.height() / 2);
		}
	});
});



// This Method retrieves all changeIds list, project details and all Patches of most updated changes
function getChangeIdDetails( key ) {
	var listItems = "";
	var xmlhttp;
	
	document.getElementById("containerId").style.display = "none";
	document.getElementById("wait").style.display = "block";
	
	if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera,// Safari
		xmlhttp = new XMLHttpRequest();
	} else { // code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var jsonData = xmlhttp.responseText;
			
			var parsedChangeJSON = JSON.parse(xmlhttp.responseText).change;
			document.getElementById("owner").innerHTML = parsedChangeJSON.owner;
			document.getElementById("uploadedon").innerHTML = parsedChangeJSON.creationDate;
			document.getElementById("updatedon").innerHTML = parsedChangeJSON.lastUpdationDate;
			document.getElementById("branch").innerHTML = parsedChangeJSON.branch;
			document.getElementById("project").innerHTML = parsedChangeJSON.projectName;
			document.getElementById("commitmsg").innerHTML = parsedChangeJSON.subject;
			var totalPatchSet = parsedChangeJSON.patchSets.length;
			multiArray = new Array(totalPatchSet);
			for ( var i = 0; i < parsedChangeJSON.patchSets.length; i++) {
				var patch = parsedChangeJSON.patchSets[i];
				var tbody = "<tbody><tr data-tt-id='1'><td><span class='folder'>Patch "
						+ (i + 1) + "</span></td></tr>";
				var totalPatches = patch.patchList.length;
				multiArray[i] = new Array(totalPatches);
				for ( var j = 0; j < patch.patchList.length; j++) {
					tbody += "<tr data-tt-id='1-"
							+ (j + 1)
							+ "' data-tt-parent-id='1'><td><span class=\"file\"><a onclick=\"fetchDatafromURL('"
							+ patch.patchList[j].fileURL + "','" + j
							+ "')\" style=\"padding-left: 19px;\"> "
							+ patch.patchList[j].patchFileName
							+ "</a></span></td></tr>"
					multiArray[i][j] = "<a onclick=\"PatchComparison('"
							+ patch.patchList[j].fileURL + "')\"> " + (i + 1)
							+ "</a>"

				}
				var id = "patch" + (i + 1);
				tbody += "</tbody>";
				var table = "<table id=\"patch" + (i + 1)
						+ "\" cellpadding=\"0\" cellspacing=\"0\">" + tbody
						+ " </table>";
				document.getElementById(id).innerHTML = table;
			}
			if (parsedChangeJSON.patchSets.length == 1) {
				document.getElementById("patch2").innerHTML = '<table id="patch2" cellpadding="0" cellspacing="0"><tbody></tbody></table>';
				document.getElementById("patch3").innerHTML = '<table id="patch3" cellpadding="0" cellspacing="0"><tbody></tbody></table>';
			} else if (parsedChangeJSON.patchSets.length == 2) {
				document.getElementById("patch3").innerHTML = '<table id="patch3" cellpadding="0" cellspacing="0"><tbody></tbody></table>';
			}
			
		//	document.getElementById("containerId").style.display = "block";
			document.getElementById("wait").style.display = "none";
		}
	}
	
	xmlhttp.open("GET", "../gerritPlugin?id="+ key, false);
	xmlhttp.send();
}

function diffSelectedChange( anchor ){
	var key = anchor.innerHTML;
	diffUsingJS(baseChangeArray[key], patchChangeArray[key], null );
}

function displayCommentHolder(e)
{
	var table=document.getElementById("diffOutTable");
	var rowIndex = e.target.parentNode.rowIndex;
	var cellIndex = e.target.cellIndex;
    var row=table.insertRow(rowIndex);
    var textarea = document.createElement('textarea');
    textarea.setAttribute('rows', '4');
    textarea.setAttribute('cols', '40');
    
    var paragraph = document.createElement('p');
    paragraph.setAttribute('style', 'padding-bottom: 10px;font-family: sans-serif;');
    
    var cell1=row.insertCell(0);
    cell1.setAttribute('class', 'lineNumber');
    var cell2=row.insertCell(1);
    cell2.setAttribute('style', 'background: #e5ecf9');
    var cell3=row.insertCell(2);
    cell3.setAttribute('class', 'lineNumber');
    var cell4=row.insertCell(3);
    cell4.setAttribute('style', 'background: #e5ecf9');
    
    var draft = document.createElement('div');
    draft.setAttribute('style', 'font-weight: bold;font-family: sans-serif;padding-bottom: 4px;');
    draft.innerHTML = "(Draft)";
    
    var divTag = document.createElement("div");
    divTag.id = "commentPanelButtons";
    divTag.setAttribute('style', 'padding-bottom: 5px;');
    
    var saveButton = document.createElement("Button");
    saveButton.innerHTML = "Save";
    saveButton.setAttribute('class', 'panelButton'); 
    saveButton.setAttribute('onclick','saveReviewerComment(event)');
    divTag.appendChild(saveButton);
    
    var editButton = document.createElement("Button");
    editButton.innerHTML = "Edit";
    editButton.setAttribute('class', 'panelButton');
    editButton.setAttribute('style', 'display: none;'); 
    editButton.setAttribute('onclick','editReviwerComment(event)');
    divTag.appendChild(editButton);
    
    var cancelButton = document.createElement("Button");
    cancelButton.innerHTML = "Cancel";
    cancelButton.setAttribute('class', 'panelButton'); 
    cancelButton.setAttribute('style', 'display: none;');
    cancelButton.setAttribute('onclick','cancel(event)');
    divTag.appendChild(cancelButton);
    
    var discardButton = document.createElement("Button");
    discardButton.innerHTML = "Discard";
    discardButton.setAttribute('class', 'panelButton'); 
    discardButton.setAttribute('onclick','deleteTableRow(event)');
    divTag.appendChild(discardButton);
    
    if( cellIndex == 1 ){
    	cell2.appendChild(draft);
        cell2.appendChild(textarea);
        cell2.appendChild(paragraph);
        cell2.appendChild(divTag);
    }else{
    	cell4.appendChild(draft);
        cell4.appendChild(textarea);
        cell4.appendChild(paragraph);
        cell4.appendChild(divTag);
    }
}


function cancel(e){
	var tableColumn = e.target.parentNode.parentNode;
	var element = tableColumn.getElementsByTagName('textarea');
	element[0].style.display = "none";
	
	var p = tableColumn.getElementsByTagName('p');
	p[0].style.display = "block";
	
	var divTag = e.target.parentNode;
	var buttons = divTag.getElementsByTagName('button');
	buttons[0].style.display = "none";
	buttons[1].style.display = "inline";
	buttons[2].style.display = "none";
	buttons[3].style.display = "none";
}


function deleteTableRow(e){
	var rowIndex = e.target.parentNode.parentNode.parentNode.rowIndex;
	var cellIndex = e.target.parentNode.parentNode.cellIndex; 
	var tableColumn = e.target.parentNode.parentNode;
	var element = tableColumn.getElementsByTagName('textarea');
	var draftMessage = element[0].value;
	document.getElementById("diffOutTable").deleteRow(rowIndex);
	if( draftMessage != ''){
		var xmlhttp;
		
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		} else { 
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				var jsonData = xmlhttp.responseText;
			}
		}
		
		xmlhttp.open("GET", "../patchDetailService?lineNumber=" + rowIndex
				+ "&side=" + cellIndex + "&message=" + draftMessage +"&changeDetail="+lastClickedUrl+"&flag=discard", true);
		xmlhttp.send();
	}
	
}


function editReviwerComment(e){
	var tableColumn = e.target.parentNode.parentNode;
	var element = tableColumn.getElementsByTagName('textarea');
	element[0].style.display = "block";
	
	var p = tableColumn.getElementsByTagName('p');
	p[0].style.display = "none";
	
	element[0].value = p[0].innerHTML;
	
	var divTag = e.target.parentNode;
	var buttons = divTag.getElementsByTagName('button');
	buttons[0].style.display = "inline";
	buttons[1].style.display = "none";
	buttons[2].style.display = "inline";
	buttons[3].style.display = "inline";
}


function saveReviewerComment(e){
	var rowIndex = e.target.parentNode.parentNode.parentNode.rowIndex;
	var cellIndex = e.target.parentNode.parentNode.cellIndex; 
	var tableColumn = e.target.parentNode.parentNode;
	var element = tableColumn.getElementsByTagName('textarea');
	var draftMessage = element[0].value;
	
	if( draftMessage != ''){
		element[0].style.display = "none";
		var p = tableColumn.getElementsByTagName('p');
		p[0].style.display = "block";
		p[0].innerHTML = draftMessage;
		
		var divTag = e.target.parentNode;
		var buttons = divTag.getElementsByTagName('button');
		buttons[0].style.display = "none";
		buttons[3].style.display = "none";
		buttons[2].style.display = "none";
		buttons[1].style.display = "inline";
		
		var xmlhttp;
		
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		} else { 
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				var jsonData = xmlhttp.responseText;
			}
		}
		xmlhttp.open("GET", "../patchDetailService?lineNumber=" + rowIndex
				+ "&side=" + cellIndex + "&message=" + draftMessage +"&changeDetail="+lastClickedUrl+"&flag=save", true);
		xmlhttp.send();
	}
}