function initImages(data){
    initImagesGeneral(data["images"])
}

function initImagesForOverview(data){
    initImagesGeneral(data["newestImages"])
}


function initImagesGeneral(images) {

    var markup = ""

    for (x in images) {

        var img = images[x]
        var path = "/api/images/getImage/" + img["path"]


        markup += "<div class='col-lg-offset-1 col-lg-10 col-md-4 col-sm-4 col-xs-6' style='padding-left: 5px;padding-right: 5px;'>"
        markup += "<div class='thumbnail'>"
        markup += "<a title='" + img["name"] + "' href='" + path + "'>"
        markup += "<img class='img-thumbnail' src='" + path + "'/>"
        markup += "</a>"
        markup += "<span class='caption'>"

        markup += "<span style='float:left;padding-left: 5px;'><b>" + img["name"] + "</b></span>"

        var imgLat = img["location"]["latitude"]
        var imgLon = img["location"]["longitude"]

        if(imgLat != -1 && imgLon != -1){
            markup += "<span style='float:right;padding-right: 5px;'><a class='glyphicon glyphicon-map-marker' href='#map' onclick='zoomToPosition(" + imgLat + "," + imgLon + ")''></a></span>"
        }
        markup += "</span></div></div>"
//
//        $("#images").append(
//                "<div class='col-md-offset-1 col-md-10 col-sm-offset-1 col-sm-10 '>" +
//                    "<a title='" + img["name"] + "' href='" + path + "'>" +
//                        "<img class='img-thumbnail' src='" + path + "'/>" +
//                    "</a>" +
//                "</div>"
//        )
    }


    $("#images").append(markup)

}



