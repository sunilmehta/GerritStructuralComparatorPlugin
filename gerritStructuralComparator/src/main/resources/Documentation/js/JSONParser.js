var multiArray = "";

var lastClickedUrl = "";

var changes = 0;

var counter = 0;

var modifiedMethod = [];

var baseVersion = "", patchVersion = "";

// This Method compares selected Patches with BaseVersion
function PatchComparison(patchSetUrl) {
	document.getElementById("containerId").style.display = "none";
	document.getElementById("wait").style.display = "block";
	if (patchSetUrl == lastClickedUrl) {
		baseVersion = "No Difference Found";
		patchVersion = "";
	} else {
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		} else {
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				var comparatorResult = null;
				var result = null;
				comparatorResult = xmlhttp.responseText;
				var javaCode = /JavaCode/g;
				result = javaCode.test(comparatorResult);
				if (result == true) {
					$('#ModificationDetails').treetable('destroy');
					document.getElementById("ModificationDetails").innerHTML = '';
					baseVersion = "";
					patchVersion = comparatorResult.replace('JavaCode', '');
					diffUsingJS(baseVersion, patchVersion);
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
	//document.getElementById("containerId").style.display = "none";
	document.getElementById("wait").style.display = "block";
	
	var comparatorResult = "";
	var result = "";
	var xmlhttp;

	lastClickedUrl = url;

	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			comparatorResult = xmlhttp.responseText;
			var javaCode = /JavaCode/g;
			result = null;
			result = javaCode.test(comparatorResult);

			var patchSetLabel = "";
			var baseSetLabel = "";
			for ( var i = 0; i < multiArray.length; i++) {
				patchSetLabel += " ";
				patchSetLabel += multiArray[i][patchNo];
			}

			document.getElementById("patchSetLabel1").innerHTML = patchSetLabel;
			document.getElementById("patchSetLabel2").innerHTML = patchSetLabel;

			if (result == true) {
				$('#ModificationDetails').treetable('destroy');
				document.getElementById("ModificationDetails").innerHTML = '';
				baseVersion = "";
				patchVersion = comparatorResult.replace('JavaCode', '');
				diffUsingJS(baseVersion, patchVersion);
			} else {
				parseJSONResponse(comparatorResult);
			}
			document.getElementById("wait").style.display = "none";
			document.getElementById("containerId").style.display = "block";
		}
		
	}
	xmlhttp.open("GET", "../gerritPlugin?url=" + url, true);
	xmlhttp.send();
}


// This Method parses the JSONResponse received from the provided URL
function parseJSONResponse(comparatorResult) {
	var parsedJSON = JSON.parse(comparatorResult);
	counter = -1;
	changes = 0;
	baseVersion = "";
	patchVersion = "";
	modifiedMethod = [];
	var changedFileTreeStructure = "<table id=\"ModificationDetails\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr data-tt-id='1'><td><span class='compilationUnit'>Compilation Unit </span></td></tr> <tr data-tt-id='1-1' data-tt-parent-id='1'><td><span class='javaIcon'>";

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
			modifiedMethod.push(parsedJSON.types[i].declarations[0].name);
		} else if (parsedJSON.types[i].diff == -1) {
			baseVersion += '\n';
			patchVersion += '\n';
			patchVersion += parsedJSON.types[i].declarations[1].completeNodeValue;
			modifiedMethod.push(parsedJSON.types[i].declarations[1].name);
		} else if (parsedJSON.types[i].diff == 10) {
			baseVersion += '\n';
			baseVersion += parsedJSON.types[i].declarations[0].completeNodeValue;
			patchVersion += '\n';
			patchVersion += parsedJSON.types[i].declarations[1].completeNodeValue;
			changedFileTreeStructure += parsedJSON.types[i].declarations[0].name
					+ "</span></td></tr>";

			for ( var k = 0; k < parsedJSON.types[i].commonChilds.length; k++) {
				if (parsedJSON.types[i].commonChilds[k].diff == 10) {
					modifiedMethod
							.push(parsedJSON.types[i].commonChilds[k].declarations[0].name);
					var commonChilds = parsedJSON.types[i].commonChilds[k].commonChilds;
					for ( var x = 0; x < commonChilds.length; x++) {
						if (commonChilds[x].diff == 1) {

						} else if (commonChilds[x].diff == -1) {

						}
					}
				} else if (parsedJSON.types[i].commonChilds[k].diff == -1) {
					modifiedMethod
							.push(parsedJSON.types[i].commonChilds[k].declarations[1].name);
				} else if (parsedJSON.types[i].commonChilds[k].diff == 1) {
					modifiedMethod
							.push(parsedJSON.types[i].commonChilds[k].declarations[1].name);
				}
			}
		}
	}

	for ( var i = 0; i < modifiedMethod.length; i++) {
		changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1)
				+ "' data-tt-parent-id='1-1'><td><span class=\"javaChangesFile\">" + modifiedMethod[i]
				+ "</span></td></tr>"
	}

	changedFileTreeStructure += "</tbody></table>"
	document.getElementById("ModificationDetails").innerHTML = changedFileTreeStructure;

	if (baseVersion == "" && patchVersion == "") {
		$('#ModificationDetails').treetable('destroy');
		document.getElementById("ModificationDetails").innerHTML = '';
		baseVersion = "No changes Found";
		patchVersion = "No changes Found";
	}
	diffUsingJS(baseVersion, patchVersion);
	$(".hide_Rows").hide();
	$('#ModificationDetails').treetable('destroy');
	createClassStructure('#ModificationDetails');
}

function diffUsingJS(baseVersion, patchVersion) {
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
		viewType : 0
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
	var w = $(".vScroll");
	$(".vScroll").scrollTop(_topoffset - w.height() / 2);
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
	var w = $(".vScroll");
	$(".vScroll").scrollTop(_topoffset - w.height() / 2);

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
		var w = $(".vScroll");
		$(".vScroll").scrollTop(_topoffset - w.height() / 2);
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
		var w = $(".vScroll");
		$(".vScroll").scrollTop(_topoffset - w.height() / 2);
	});
});



// This Method retrieves all changeIds list, project details and all Patches of most updated changes
function getChangeIdDetails() {
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
			if (document.getElementById("changeId").value == "") {
				var parsedJSON = JSON.parse(xmlhttp.responseText).changeIDs;
				var parsedChangeJSON = JSON.parse(xmlhttp.responseText).change;
				parsedJSON.reverse();
				for ( var i = 0; i < parsedJSON.length; i++) {
					listItems += "<option value='" + parsedJSON[i].change_id
							+ "'>" + parsedJSON[i].change_id + "</option>";
				}
				document.getElementById("changeId").innerHTML = listItems;
			}
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
	if (document.getElementById("changeId").value == "") {
		xmlhttp.open("GET", "../gerritPlugin", false);
	} else {
		xmlhttp.open("GET", "../gerritPlugin?id="
				+ document.getElementById("changeId").value, false);
	}
	xmlhttp.send();
}