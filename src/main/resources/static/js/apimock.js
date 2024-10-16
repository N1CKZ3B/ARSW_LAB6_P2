//@author hcadavid

apimock=(function(){

	var mockdata=[];

	mockdata["nico"] = [
		{author: "nico", "points": [{"x": 140, "y": 140}, {"x": 115, "y": 115}], "name": "house_design"}
	];

	mockdata["richi"] = [{author: "richi", "points": [{"x": 150, "y": 150}, {"x": 120, "y": 120}], "name": "office_layout"},
		{author: "richi", "points": [{"x": 170, "y": 170}, {"x": 130, "y": 130}], "name": "kitchen_remodel"}];

	mockdata["tommy"] = [
		{author: "tommy", "points": [{"x": 160, "y": 160}, {"x": 125, "y": 125}], "name": "garden_plan"}
	];


	return {
		getBlueprintsByAuthor:function(authname,callback){
			callback(
				mockdata[authname]
			);
		},

		getBlueprintsByNameAndAuthor:function(authname,bpname,callback){
			callback(
				mockdata[authname].find(function(e){return e.name===bpname})
			);
		}
	}

})();

