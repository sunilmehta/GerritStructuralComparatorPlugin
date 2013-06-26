(function() {

    "use strict"
    var vrSplitter = document.getElementById('splitHalf');						// : Leftside Patch Tree and Streaming Splitter 
    var splitter = document.getElementsByClassName('splitter');					// : Getting All Splitter classes (Top, Left, and Left Bottom )
    var splitterTargetedContent = document.getElementsByClassName('content');	// : Getting All content classes which are in splitter		
    var codeReviewViewState = '';												// : Defines the Code Review Section States
    var patchesSection = document.getElementById('navController');				// : Getting the Left Patch / Tree Section Id
    var toggleCodeReviewViews = document.getElementsByClassName('controlScreen'); // :  Getting Code Review Section Class

	// Performing a click action on (Top or Left) splitters
    for (var i = 0; i < splitter.length; i++) {
        splitter[i].onclick = function() {
            splitterAction(this)
            }
    }

	// Resizing the patch tree section on with min-height and max height;
    vrSplitter.onclick = function() {
        patchesSection.className = (patchesSection.className == 'expandTree') ? 'collapseTree': 'expandTree';
    }

	// Toggleing the splitter content by showing and hiding with css class
    function splitterAction(targetStateDiv) {
        var checkSpliters = targetStateDiv.parentNode;
        checkSpliters.className = (checkSpliters.className == 'showContent') ? 'hideContent': 'showContent';
    }

	// Toggling a click action using resize/full icon to control the Code Revew Diff Section
    for (var i = 0; i < toggleCodeReviewViews.length; i++) {
        toggleCodeReviewViews[i].onclick = function() {
            if (this.click) {
                toggleCodeReviewAction(this);
            }
        }
    }

	// Toggling the view (resize/full) of Code Revew Diff Section
    function toggleCodeReviewAction(codeReviewState) {
        if (codeReviewViewState == '') {
            splitTarget('show', 'hideContent');
            codeReviewState.parentNode.className = 'showResizedScreen';
            document.getElementById('diffoutput').className = 'vScrollFullScreen';
        } else {
            splitTarget('', 'showContent');
            codeReviewState.parentNode.className = 'showFullScreen';
            document.getElementById('diffoutput').className = 'vScroll';
        }
    }

	// Assigning the css class to splitted section (TOP, LEFT, LEFT BOTTOM)
    function splitTarget(toggleCodeReviewStates, toggleOtherSplitters) {
        for (var i = 0; i < splitterTargetedContent.length; i++) {
            splitterTargetedContent[i].parentNode.className = toggleOtherSplitters;
            codeReviewViewState = toggleCodeReviewStates;
        }
    }

})();

// Patch Object
// This is kind of patch factory, where you can play around with Patch Ids and assign the final one to the getPatchId Method.
var patch = function() {
    // Private
    var patchId = '#patch1'; // default patch
    return {
        getPatchId: function() {
            return patchId;
        },
        setPatchId: function(newName) {
            patchId = '#' + newName;
        }
    };
} ();

// Loading Default Patch...

function init( anchor ) {
	var changeID = anchor.innerHTML;
	getChangeIdDetails( changeID );
	patch.setPatchId('patch1');
	console.log(patch.getPatchId() + ' is fired from init dropdown');
	// Added by: Amit.Anjani
    $(patch.getPatchId()).treetable('destroy');
    // End
	comparePatchIds(patch.getPatchId());
	document.getElementById("changeID").innerHTML = changeID; 
	document.getElementById("checkCurrentPatch").innerHTML = 'Patch 1';
	document.getElementById("ShowDetail").style.display="block";
	document.getElementById("HideDetail").style.display="none";
	document.getElementById("dialogBox").style.display= "none";
	$('#dialog_StatusOpen_window_minimized').show();
	$('#dialog_StatusOpen_window').hide();
	$('#headerSection').hide();
    $('#dataContainer').show();
	}

function loadGerritCommit() {
	getGerritCommitDetails();
}

window.load = loadGerritCommit();

// This action selects a patch from the drop down and sends 'this' data-dd-id to patch Object by using setPatchId
// Also send final data-dd-id to the comparePatchIds as param.
$('.subMenu a').click(function() {
    patch.setPatchId($(this).attr('data-dd-id'));
    console.log(patch.getPatchId() + ' is fired from dropdown');
    comparePatchIds(patch.getPatchId());
});

/* 	Comparision (DATA-DD-ID and TABLE ID)
	This Function compares both table Id and getPatchId Method(data-dd-id)
	if its true then
	1. Adds class "showThisTable" to the matched table to visible it
	2. And sends getPatchId as a param to the renderPatchTree function

	or if false then
	1. removes the 'showThisTable' class if found then adds the 'hideThisTable' class to hide the tables */

function comparePatchIds(getPatchInfo) {
    var treeTables = $('.treeStructure table');
    for (var i = 0; i < treeTables.length; i++) {
        if (getPatchInfo.indexOf(treeTables[i].getAttribute('id')) !== -1) {
            treeTables[i].className = 'showThisTable treetable';
            renderPatchTree(getPatchInfo);
        } else {
            if (treeTables[i].className === 'showThisTable') {
                treeTables[i].className = '';
            }
            treeTables[i].className = 'hideThisTable';
        }
    }
}


// Added By: Amit.Anjani
function createClassStructure(getPatchInfo) {
    var treeTables = $('.treeClassStructure table');
    for (var i = 0; i < treeTables.length; i++) {
        if (getPatchInfo.indexOf(treeTables[i].getAttribute('id')) !== -1) {
            treeTables[i].className = 'showThisTable treetable';
            renderPatchTree(getPatchInfo);
        } else {
            if (treeTables[i].className === 'showThisTable') {
                treeTables[i].className = '';
            }
            treeTables[i].className = 'hideThisTable';
        }
    }
}



// 	Rendering Selected Patch Tree using the final getPatchId from the Patch Object as a param called 'getFinalPatchId'
function renderPatchTree(getFinalPatchId) {

    var patchIdSelector = getFinalPatchId;
    
    $(patchIdSelector).treetable({
        expandable: true
    });

    // Highlight selected row
    $(patchIdSelector + " tbody tr").mousedown(function() {
        $("tr.selected").removeClass("selected");
        $(this).addClass("selected");
    });

    console.log(patchIdSelector);
    $(patchIdSelector + " .folder").each(function() {

        $(this).parents("tr").droppable({
            accept: ".file, .folder",
            drop: function(e, ui) {
                var droppedEl = ui.draggable.parents("tr");
                $(patchIdSelector).treetable("move", droppedEl.data("ttId"), $(this).data("ttId"));
            },
            hoverClass: "accept",
            over: function(e, ui) {
                var droppedEl = ui.draggable.parents("tr");
                if (this != droppedEl[0] && !$(this).is(".expanded")) {
                    $(patchIdSelector).treetable("expandNode", $(this).data("ttId"));
                }
            }
        });
    });

    // Initialize the plugin for the elements with the class "dropdown"
    $(".dropdown").dropdown();

    // For this demo, show the original selects instead of leaving them hidden
    $("select.dropdown").css("opacity", .5).show().find(" + .dropdown").wrap("<div />");

    // Code for the stylesheet switcher
    var stylesheets = $("link"),
    stylesLink = $("#stylesLink span");

    $("select[name='styles']").hide().change(function() {
        var active = stylesheets.not(stylesheets.not("[title='" + $(this).val() + "']").attr("disabled", "disabled")).removeAttr("disabled"),
        href = active.attr("href");
        if (!href) {
            stylesLink.text("inline styles");
        } else {
            stylesLink.empty().append($(document.createElement("a")).text(href).attr("href", href));
        }
    });

    var tableLength = $(patchIdSelector + '> tbody').children().each(function() {
        $(this).addClass('myClass');
    });

	// 	Applying css classes based on the extention. 
    $(patchIdSelector + '	td:has(span:contains("jpg"))').addClass('image');
    $(patchIdSelector + ' td:has(span:contains("png"))').addClass('image');
    $(patchIdSelector + ' td:has(span:contains("gif"))').addClass('image');
    $(patchIdSelector + ' td:has(span:contains("pdf"))').addClass('pdf');
    $(patchIdSelector + ' td:has(span:contains("zip"))').addClass('zip');

	// 	Expanding tree 1 level 
    $(patchIdSelector).treetable('expandNode', '1');
}
 (function() {
    var toggleCommentedFile = 'showFiles';		// : Toggling the states of the comments icon button
    var collectComments = [];					// : Collecting the Commented Files data-dd-ids
    var collectSearches = [];					// : Collecting the Searched and Matched Files data-dd-ids

	
	// Search Error
	// Performaing when the search content fails to match the files contents. 
    var fileSearchError = {

        "fileSearchErrorSelector": '.fileNotFoundValid',
        "overlay": '.overlay',

		// Showing the Error Box with Overlay 
        "errorHide": function(errorBoxDelay, errorContent, overlayDelay, errorOverlay) {
            return $(this.fileSearchErrorSelector).delay(errorBoxDelay).fadeOut(errorContent),
            $(this.overlay).delay(overlayDelay).fadeOut(errorOverlay);
        },
		
		// Hiding the Error Box with Overlay
        "errorShow": function(errorBoxDelay, errorContent, overlayDelay, errorOverlay) {
            return $(this.fileSearchErrorSelector).delay(errorBoxDelay).fadeIn(errorContent),
            $(this.overlay).delay(overlayDelay).fadeIn(errorOverlay);
        }

    };
	
	// As Default Hiding the Error Box with Overlay
    fileSearchError.errorHide(0, 0, 0, 0);

	// Show and Hide Commented Files
	// Toggeling the Commented Files by Clicking comment Icons with counts. 
    $('.commentCounts').click(function() {
        $(patch.getPatchId()).treetable('collapseAll'); // Collapsing all the tree

        collectComments = []; // Removing the file data-dd-id to load the new data

		
		// for comments icon toggeling css classes
		
        if (toggleCommentedFile === 'showFiles') {

            $(this).removeClass('commentCountsOn');
            $(this).addClass('commentCountsOff');

            toggleCommentedFile = '';

            var getCommentFile = '.comment';

            $(patch.getPatchId()).treetable('collapseAll');

			// If the above this files true then collecting the file data-dd-id and pushing collectComments Array
            $(patch.getPatchId() + " .comment").each(function() {
                collectComments.push($(this).parents('tr').attr('data-tt-id'));
            })
				
				// and placing the complete collection of array as param in patchTreeResult
                patchTreeResult(collectComments);

        } else {

            if (toggleCommentedFile === '') {
                $(patch.getPatchId() + " tr").removeAttr('data-add-tr');
                $(patch.getPatchId()).treetable('collapseAll');
                $(patch.getPatchId()).treetable('expandNode', '1');

                $(this).removeClass('commentCountsOff');
                $(this).addClass('commentCountsOn');

            }

            toggleCommentedFile = 'showFiles';
        }

    });

	// Search the Patch File by Press Enter Key, after focusing in the search box
    $('#search').keyup(function(event) {
        if (event.keyCode == 13 && event.keyCode !== true) {
            $('#searchAction').click();
        }
    });
	
	// Search the Patch File by Clicking on the search Icon
    $("#searchAction").click(function(index) {
        searchFile();
    });

	// Search Patch Files get the data-dd-id and put in the  collectSearches arrays and patchTreeResult (as a params).
    function searchFile() {

        $(patch.getPatchId()).treetable('collapseAll');

        collectSearches = [];

        var getValue = $('#search').val();

        if (getValue.length !== 0) {

            $(patch.getPatchId() + " tr").removeAttr('data-add-tr');

            $(patch.getPatchId() + " span.file:contains('" + getValue + "')").each(function() {

                collectSearches.push($(this).parents('tr').attr('data-tt-id'));
            })

                if (collectSearches.length !== 0) {
                patchTreeResult(collectSearches);

            } else {
                fileSearchError.errorShow(150, 200, 0, 100);
                $(patch.getPatchId() + " tr").removeAttr('data-add-tr');
                $(patch.getPatchId()).treetable('collapseAll');
                $(patch.getPatchId()).treetable('expandNode', '1');
                fileSearchError.errorHide(2000, 100, 2200, 300);
            }

        } else {

            $(patch.getPatchId() + " tr").removeAttr('data-add-tr');
            $(patch.getPatchId()).treetable('collapseAll');
            $(patch.getPatchId()).treetable('expandNode', '1');

        }
    }

	// This takes collections of arrays and renders results in a tree formats.
    function patchTreeResult(getDataIdCollection) {
        this.fileDataId;
        this.fileParentsDataId;
        this.fileParentRefinedDataId;
        this.splitedFileDataId;

        for (fileDataId = 0; fileDataId < getDataIdCollection.length; fileDataId++) {

            splitedFileDataId = getDataIdCollection[fileDataId].split('-');
            fileParentRefinedDataId = '';

            for (fileParentsDataId = 0; fileParentsDataId < splitedFileDataId.length; fileParentsDataId++) {

                fileParentRefinedDataId = fileParentRefinedDataId + ((fileParentsDataId === 0) ? '': '-') + splitedFileDataId[fileParentsDataId];
                $(patch.getPatchId()).treetable('expandNode', fileParentRefinedDataId);
                $(patch.getPatchId() + " tr[data-tt-id ='" + fileParentRefinedDataId + "']").attr('data-add-tr', 'showThisTr');
                $(patch.getPatchId() + " tr[data-add-tr!= 'showThisTr']").attr('data-add-tr', 'hideThisTr');
            }

        }

    }
})();