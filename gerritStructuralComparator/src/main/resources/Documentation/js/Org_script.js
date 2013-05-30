(function () {

    "use strict"
    var vrSplitter = document.getElementById('splitHalf');
    var splitter = document.getElementsByClassName('splitter');
    var hrSplitter = document.getElementsByClassName('hr_spliter').item(0);
    var target = document.getElementsByClassName('content');
    var targetZoomState = '';
    var findNavParent = document.getElementById('navController');
    var controlScreen = document.getElementsByClassName('controlScreen');


    for (var i = 0; i < splitter.length; i++) {
        splitter[i].onclick = function () {
            splitterAction(this)
        }
    }

    vrSplitter.onclick = function () {
        findNavParent.className = (findNavParent.className == 'collapseTree') ? 'expandTree' : 'collapseTree';
    }


    function splitterAction(targetStateDiv) {
        var checkSpliters = targetStateDiv.parentNode;
        checkSpliters.className = (checkSpliters.className == 'showContent') ? 'hideContent' : 'showContent';
    }

    for (var i = 0; i < controlScreen.length; i++) {
        controlScreen[i].onclick = function () {
            if (this.click) {
                zoomAction(this);
            }
        }
    }


    function zoomAction(checkScreenAction) {
        if (targetZoomState == '') {
            splitTarget('show', 'hideContent');
            checkScreenAction.parentNode.className = 'showResizedScreen';
        } else {
            splitTarget('', 'showContent');
            checkScreenAction.parentNode.className = 'showFullScreen';
        }
    }

    function splitTarget(checkZoomState, chckTarget) {
        for (var i = 0; i < target.length; i++) {
            target[i].parentNode.className = chckTarget;
            targetZoomState = checkZoomState;
        }
    }

})(); 



$("#example-advanced").treetable({
    expandable: true
}, {
    expandAll: true
}, move = false);


// Highlight selected row
$("#example-advanced tbody tr").mousedown(function () {
    $("tr.selected").removeClass("selected");
    $(this).addClass("selected");
});
// Drag & Drop Example Code
$("#example-advanced .file, #example-advanced .folder").draggable({
    helper: "clone",
    opacity: .75,
    refreshPositions: true, // Performance?
    revert: "invalid",
    revertDuration: 300,
    scroll: true
});

$("#example-advanced .folder").each(function () {
    $(this).parents("tr").droppable({
        accept: ".file, .folder",
        drop: function (e, ui) {
            var droppedEl = ui.draggable.parents("tr");
            $("#example-advanced").treetable("move", droppedEl.data("ttId"), $(this).data("ttId"));
        },
        hoverClass: "accept",
        over: function (e, ui) {
            var droppedEl = ui.draggable.parents("tr");
            if (this != droppedEl[0] && !$(this).is(".expanded")) {
                $("#example-advanced").treetable("expandNode", $(this).data("ttId"));
            }
        }
    });
});

(function ($) {
    if (!$) return;

    $(function () {
        // Initialize the plugin for the elements with the class "dropdown"
        $(".dropdown").dropdown();

        // For this demo, show the original selects instead of leaving them hidden
        $("select.dropdown").css("opacity", .5).show().find(" + .dropdown").wrap("<div />");

        // Code for the stylesheet switcher
        var stylesheets = $("link"),
            stylesLink = $("#stylesLink span");
            
        $("select[name='styles']").hide().change(function () {
            var active = stylesheets.not(stylesheets.not("[title='" + $(this).val() + "']").attr("disabled", "disabled")).removeAttr("disabled"),
                href = active.attr("href");
            if (!href) {
                stylesLink.text("inline styles");
            } else {
                stylesLink.empty().append($(document.createElement("a")).text(href).attr("href", href));
            }
        }).change();
    })

    var tableLength = $('#example-advanced > tbody').children().each(function () {
        $(this).addClass('myClass');
    });


    $('#example-advanced td:has(span:contains("jpg"))').addClass('image');
    $('#example-advanced td:has(span:contains("png"))').addClass('image');
    $('#example-advanced td:has(span:contains("gif"))').addClass('image');
    $('#example-advanced td:has(span:contains("pdf"))').addClass('pdf');
    $('#example-advanced td:has(span:contains("zip"))').addClass('zip');

})(window.jQuery);

$('#example-advanced').treetable('expandNode', '1');

var showCommentedFiles = 'treeOpened';
var collectComments = [];
var collectSearches = [];

            
$('.commentCounts').click(function () {
	
	$('#example-advanced').treetable('collapseAll');
	
	collectComments = [];
	
    if (showCommentedFiles === 'treeOpened') {

        $(this).removeClass('commentCountsOn');
        $(this).addClass('commentCountsOff');

        showCommentedFiles = '';
        
        var getCommentFile = '.comment';

        $('#example-advanced').treetable('collapseAll');
        
        
        
        $("#example-advanced .comment").each(function(){
				collectComments.push($(this).parents('tr').attr('data-tt-id'));		
		})
		
        
         generateTree(getCommentFile, collectComments);
         
         
    } else {

        if (showCommentedFiles === '') {
			$("#example-advanced tr").removeAttr('data-add-tr');
            $('#example-advanced').treetable('collapseAll');
            $('#example-advanced').treetable('expandNode', '1');

            $(this).removeClass('commentCountsOff');
            $(this).addClass('commentCountsOn');

        }

        showCommentedFiles = 'treeOpened';
    }

});

$('#search').keyup(function (event) {
    if (event.keyCode == 13 && event.keyCode !== true) {
        $('#searchAction').click();
    }
});

$("#searchAction").click(function (index) {
    searchInput();
});

$('.fileNotFoundValid').hide();
$('.overlay').hide();
function searchInput() {
	
	
    $('#example-advanced').treetable('collapseAll');
	
	collectSearches = [];

	var getValue = $('#search').val();

	
	if(getValue.length  !== 0){
		
		
		
		$("#example-advanced tr").removeAttr('data-add-tr');
		
		$("#example-advanced span.file:contains('"+ getValue + "')").each(function(){
				
				collectSearches.push($(this).parents('tr').attr('data-tt-id'));		
		})
		
		if(collectSearches.length !== 0){
				$('.fileNotFoundValid').fadeOut(100);
				$('.overlay').delay(200).fadeOut(100);
				

				generateTree(getValue, collectSearches);

		} else {
			$('.overlay').fadeIn(100);
			$('.fileNotFoundValid').delay(150).fadeIn(100);
			$("#example-advanced tr").removeAttr('data-add-tr');
            $('#example-advanced').treetable('collapseAll');
            $('#example-advanced').treetable('expandNode', '1');
            $('.fileNotFoundValid').delay(2000).fadeOut(100);
            $('.overlay').delay(2200).fadeOut(300);
		}
		
		
		
	} else {
		
			$("#example-advanced tr").removeAttr('data-add-tr');
            $('#example-advanced').treetable('collapseAll');
            $('#example-advanced').treetable('expandNode', '1');
		
	}
}

function generateTree(getParentId, pushInArray) {

    this.i;
    this.j;
    this.substrTillNow;
    this.splits;
    this.getTrId = 'data-tt-id';
    
    for (var i = 0; i < pushInArray.length; i++) {
    	    	
		splits = pushInArray[i].split('-');
        substrTillNow = '';
        
        for (j = 0; j < splits.length; j++) {
			
            substrTillNow = substrTillNow + ((j === 0) ? '' : '-') + splits[j];
			$('#example-advanced').treetable('expandNode', substrTillNow);
			
				$("#example-advanced tr[data-tt-id ='"+substrTillNow+"']").attr('data-add-tr', 'showThisTr');
				$("#example-advanced tr[data-add-tr!= 'showThisTr']").attr('data-add-tr', 'hideThisTr');
    }
       
}

}