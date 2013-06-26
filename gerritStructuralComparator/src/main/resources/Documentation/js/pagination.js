$(document).ready(function() {
	var rows=$('#gerritCommitStatus').find('tbody tr').length;
	var no_rec_per_page=15;
	var no_pages= Math.ceil(rows/no_rec_per_page);
	var $pagenumbers=$('<div id="pages" style="float: right;"></div>');
		for(var i=0;i<no_pages;i++)
		{
			$('<span class="page">'+(i+1)+'</span>').appendTo($pagenumbers);
		}
	$pagenumbers.insertAfter('#gerritCommitStatus');
	$('.page').hover(
		function(){
			$(this).addClass('hover');
		},
		function(){
			$(this).removeClass('hover');
		}
	);
	$('#gerritCommitStatus').find('tbody tr').hide();
	var tr=$('#gerritCommitStatus tbody tr');
	for(var i=0;i<=no_rec_per_page-1;i++)
	{
		$(tr[i]).show();
	}
	$('span').click(function(event){
		$('#gerritCommitStatus').find('tbody tr').hide();
	for(i=($(this).text()-1)*no_rec_per_page;i<=$(this).text()*no_rec_per_page-1;i++)
	{
		$(tr[i]).show();
	}
	});
});