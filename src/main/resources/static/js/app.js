const toggleSidebar=()=>{
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display","none")
		$(".content").css("margin-left","0%");
		//$(".bars-btn").css("display","block");
		
	}else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
		//$(".bars-btn").css("display","hidden");
	}
}
const search=()=>{
	//console.log("typing...")
	let query = $('#search-input').val();
	
	if(query==''){
		$('.search-result').hide();
	}else{
		let url = `http://localhost:8080/search/${query}`;
		fetch(url).then((response)=>{
			return response.json();
		}).then((data)=>{
			console.log(data);
			let text = `<div class='list-group'>`;
			data.forEach((contacts)=>{
				text+= `<a href='/user/contact/${contacts.cId}' class='list-group-item list-group-item-action'>${contacts.cFirstName}</a>`;
				/*text+= `<a href='/user/contact/${contacts.cId}' class='list-group-item list-group-item-action'><img style="height:30px; width:30px;" class="profile-pic" th:src="@{'/profile_pic/'+${contacts.cImage}}"/> ${contacts.cFirstName}</a>`;*/
			});
			text+=`</div>`;
			$('.search-result').html(text);
			$('.search-result').show();
		});
		console.log(query);
		
	}
};