extractor = bmsys.extractor("2-gram");
source = fs.open(argv[1]);
birthmarks = extractor.extract(source);

// var rs = WScript.CreateObject("Scripting.FileSystemObject");
// var file = rs.CreateTextFile("text.txt");
// file.Write(birthmarks);
// file.close();
//
// fs.createFile("test.txt");
// fs.writeFile("test.txt", birthmarks);
//
fs.writer("test.txt", birthmarks)

// fs.print(birthmarks);
//
// fs.print("extraction: " + birthmarks.time() + " ns")
//

