var selectionVariable = {};

selectionVariable.selectionChange = function(box, value) {
    var dialog = box.findParentByType('dialog');
    if (value == "Add New Parent") {
        selectionVariable.resetDialog(dialog);
    } else {
        var path = dialog.path;
        var parentJson = CQ.HTTP.get(CQ.HTTP.externalize(path + "/" + value + ".infinity.json"));
        var parent = JSON.parse(parentJson.body);
        var children = (Object.keys(parent).toString()).match(/child[0-9]/g);
        var noOfChildren = 0;
        if (children) {
            noOfChildren = children.length;
        }
        dialog.getField('./parentTitle').setValue(parent.title);
        dialog.getField('./parentPath').setValue(parent.url);
        var childrenField = dialog.getField('./children');
        var childrenValue = "";
        for (var i = 0; i < noOfChildren; i++) {
            var child = JSON.parse(CQ.HTTP.get(CQ.HTTP.externalize(path + "/" + value + "/child" + i + ".json")).body);
            if (i !== 0) {
                childrenValue += ",";
            }
            childrenValue += "\"{'child_title':'" + child.title + "','child_path':'" + child.url + "'}\"";
        }
        childrenField.setValue(JSON.parse("[" + childrenValue.replace(/'/g, '\\\"') + "]"));
    }
}


selectionVariable.onClickAdd = function(button, event) {
    var dialog = button.findParentByType('dialog');
    var parentTitle = dialog.getField('./parentTitle').getValue();
    if (parentTitle) {
        var path = dialog.path;
        var selection = dialog.getField('./parentSelected');
        var itemSelected = selection.getValue();

        var noOfParents = selection.options.length + 1;
        var parent = dialog.getField('./parentPath');
        var parentPath = "";
        if (parent) {
            parentPath = parent.getValue();
        }
        var newParentName = "parent" + noOfParents;
        if (itemSelected && itemSelected != "Add New Parent") {
            newParentName = itemSelected;
            selectionVariable.deleteNode(path, selection);
        }

        CQ.HTTP.post(path + "/" + newParentName, null, {
            "jcr:primaryType": "nt:unstructured",
            async: false,
            "title": parentTitle,
            "name": newParentName,
            "url": parentPath
        });

        var children = dialog.getField('./children');
        $.each(children.getValue(), function(index, value) {
            CQ.HTTP.post(path + "/" + newParentName + "/child" + index, null, {
                "jcr:primaryType": "nt:unstructured",
                "title": JSON.parse(value).child_title,
                "url": JSON.parse(value).child_path
            });
        });
        selectionVariable.fetchData(selection);
        selectionVariable.resetDialog(dialog);
    } else {
        alert("Title cannot be Blank");
    }
}



selectionVariable.onClickDelete = function(button, event, refreshDialog) {
    var dialog = button.findParentByType('dialog');
    var path = dialog.path;
    var selection = dialog.getField('./parentSelected');
    selectionVariable.deleteNode(path, selection);
    if (refreshDialog) {
        selectionVariable.fetchData(selection);
        selectionVariable.resetDialog(dialog);
    }

}


selectionVariable.deleteNode = function(path, selection) {
    var itemSelected = selection.getValue();
    if (itemSelected && itemSelected != "Add New Parent") {
        $.ajax({
            url: path + "/" + itemSelected,
            method: "POST",
            async: false,
            data: {
                ":operation": "delete"
            }
        })
    }
}


selectionVariable.addToSelect = function(selection, text, value) {
    var options = selection.options;
    options.push({
        "value": value,
        "text": text
    })
    selection.setOptions(options);
}

selectionVariable.fetchData = function(field) {
    var dialog = field.findParentByType('dialog');
    var path = dialog.path;
    var listCompJson = CQ.HTTP.get(CQ.HTTP.externalize(path + ".infinity.json"));
    if (listCompJson) {
        var parents = JSON.parse(listCompJson.body);
        var parentNodes = (Object.keys(parents).toString()).match(/parent[0-9]/g);
        var selection = dialog.getField('./parentSelected');
        selection.options = [];
        selection.setOptions(selection.options);
        selection.options.push({
            "value": "Add New Parent",
            "text": "Add New Parent"
        });
        $(parentNodes).each(function(index, value) {
            var parentNode = CQ.HTTP.get(CQ.HTTP.externalize(path + "/" + value + ".json"));
            if (parentNode.status != 404) {
                var parent = JSON.parse(parentNode.body);
                selectionVariable.addToSelect(selection, parent.title, parent.name);
            }
        });
    }
}


selectionVariable.resetDialog = function(dialog) {
    var path = dialog.path;
    dialog.getField('./parentTitle').reset();
    dialog.getField('./parentPath').reset();
    dialog.getField('./children').reset();
}

selectionVariable.afterRender = function(field){
var dialog=field.findParentByType('dialog');
selectionVariable.resetDialog(dialog);
} 

