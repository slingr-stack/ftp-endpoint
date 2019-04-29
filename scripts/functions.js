/////////////////////
// Public API
/////////////////////

endpoint.uploadFile = function (folder, fileId) {
    if (!fileId) {
        fileId = folder;
        folder = null;
    }
    if (!fileId) {
        throw 'Empty file id';
    }
    var options = {
        folder: folder,
        fileId: fileId
    };
    checkValue(options, 'fileId');
    return endpoint._uploadFile(options);
};

////////////////////////
//  Private helpers
///////////////////////

var checkValue = function (options, idKey) {
    if (!options[idKey]) {
        // exception if value is not present
        throw 'Empty ' + idKey;
    }
};

