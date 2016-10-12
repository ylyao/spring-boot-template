/**
 * Created by yaoxun on 2016/7/16.
 */
require.config({
    urlArgs: "bust=1",
    paths : {
        text: 'js/libs/text',
        jquery: 'js/libs/jquery-1.11.1.min',
        cookie: 'js/libs/jquery.cookie',
        table: 'js/table',
        underscore: 'js/libs/underscore',
        socket: 'js/libs/socket.io',
        util: 'js/util',
        load: 'js/load',
        avalon: 'js/libs/avalon.modern',
        avalon2: 'js/libs/avalon2.modern',
        bootstrap: 'bootstrap/js/bootstrap.min',
        alertify: 'alertifyjs/alertify.min',
        markdown: 'markdown/markdown-it.min',
        amap: "http://webapi.amap.com/maps?v=1.3&key=842388b0154da76a95960cdc9618406a&callback=init"
    },
    shim : {
        jquery: {
            exports : '$'
        },
        bootstrap : {
            exports : 'bootstrap',
            deps: ['jquery']
        },
        avalon: {
            exports : 'avalon'
        },
        socket: {
            exports : 'socket'
        }
    }
});