(function($) {
var plugin = CQ.Ext.extend(CQ.Ext.emptyFn, {
    init : function(widget) {
       var dialog = widget.findParentByType('dialog');
       var rootPath = dialog.getField("./rootPage").value;
        widget.treeRoot.name = rootPath.substring(1);
    }
});
CQ.Ext.ComponentMgr.registerPlugin('customRootPathPlugin', plugin);}($CQ));

