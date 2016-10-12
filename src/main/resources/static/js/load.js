/**
 * Created by yaoxun on 2016/9/23.
 */
define( ["jquery",'alertify'], function($,alertify) {


    var cache = {};

    var cachestatic = {};

    var loadstatic = function (url,callback) {

        var cacheValue = cachestatic[url];

        if (!cacheValue){

            var sy = true;

            if (!callback){
                sy = false;
            }

            $.ajax({
                url: url,
                type: "get",
                async: sy,
                success: function (data, textStatus) {
                    cache[name] = data;
                    if (callback){
                        callback(data);
                    }
                },
                error: function (XMLHttpRequest, textStatus) {
                    alertify.error("模板加载失败");
                }
            });
        }

        if (callback && !sy){
            callback(cacheValue)
        }

        return cacheValue;


    };

    var load = function (name,callback) {

        var cacheValue = cache[name];

        if (!cacheValue){

            var sy = true;

            if (!callback){
                sy = false;
            }

            $.ajax({
                url: "template/" + name,
                type: "get",
                async: sy,
                success: function (data, textStatus) {
                    cache[name] = data;
                    if (callback){
                        callback(data);
                    }
                },
                error: function (XMLHttpRequest, textStatus) {
                    alertify.error("模板加载失败");
                }
            });
        }

        if (callback && !sy){
            callback(cacheValue)
        }

        return cacheValue;
    };

    return {
        load : load,
        loadstatic : loadstatic
    }

});
