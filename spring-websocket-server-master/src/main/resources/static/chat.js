$(function() {
    'use strict';

    var client;

    function showMessage(mesg)
    {
	$('#messages').append('<tr>' +
			      '<td>' + mesg.from + '</td>' +
			      '<td>' + mesg.topic + '</td>' +
			      '<td>' + mesg.message + '</td>' +
			      '<td>' + mesg.time + '</td>' +
			      '</tr>');
    }

    function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	//$('#from').prop('disabled', connected);
	$('#text').prop('disabled', !connected);
	if (connected) {
	    $("#conversation").show();
	    $('#text').focus();
	}
	else $("#conversation").hide();
	$("#messages").html("");
    }

    $("form").on('submit', function (e) {
	e.preventDefault();
    });
/*
    $('#from').on('blur change keyup', function(ev) {
	$('#connect').prop('disabled', $(this).val().length == 0 );
    });
*/
    $('#disconnect,#text').prop('disabled', true);

    $('#connect').click(function() {
    var sock = new SockJS('http://ms.pexa.local:9090/chat');
	client = Stomp.over(sock);
//	client = Stomp.over(new SockJS('/chat'));
	client.connect({}, function (frame) {
	    setConnected(true);
	    //var userId = $('#from').val();
	    client.subscribe('/user/topic/messages', function (message) {
	    		showMessage(JSON.parse(message.body));
	    });
	    client.subscribe('/topic/global/messages', function (message) {
    			showMessage(JSON.parse(message.body));
	    });

	});
    sock.onclose = function(){ alert("Socket Closed!"); };

    });

    $('#disconnect').click(function() {
	if (client != null) {
	    client.disconnect();
	    setConnected(false);
	}
	client = null;
    });

    
    $('#send').click(function() {
	var topic = $('#topic').val();
	var receiver = $('#receiver').val();
	client.send("/app/chat/v5/" + receiver, {}, JSON.stringify({
	    from: $("#from").val(),
	    text: $('#text').val(),
	}));
	$('#text').val("");
    });
});
