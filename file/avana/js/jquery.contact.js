(function($) { 
	"use strict";
	
jQuery(document).ready(function(){
	$('#cform').on( "submit", function() {

		var action = $(this).attr('action');

		$("#message").slideUp(750,function() {
		$('#message').hide();

 		$('#submit')
			.before('<div class="text-center"><img src="images/ajax-loader.gif" class="contact-loader" /></div>')
			.attr('disabled','disabled');

		$.post(action, {
			name: $('#name').val(),
			companyname: $('#companyname').val(),
			email: $('#email').val(),
			tel: $('#tel').val(),
			comments: $('#comments').val(),

		},
			function(data){
				document.getElementById('message').innerHTML = data;
				$('#message').slideDown('slow');
				$('#cform img.contact-loader').fadeOut('slow',function(){$(this).remove()});
				$('#submit').removeAttr('disabled');
				if(data.match('success') != null) $('#cform').slideUp('slow');
			}
		);

		});

		return false;

	});

});

}(jQuery));