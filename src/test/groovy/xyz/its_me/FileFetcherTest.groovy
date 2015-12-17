package xyz.its_me

import groovy.transform.ToString
import spock.lang.Specification

@ToString
class MockFile {
    boolean folder
    String name
    List<MockFile> children
}

class FileFetcherTest extends Specification {
    def mock() {
        given:
        def file01 = new MockFile(name: 'testfile01.txt')
        def subFolder = new MockFile(folder: true, name: 'subfolder', children: [file01])
        def file02 = new MockFile(name: 'testfile02.txt')
        def rootFolder = new MockFile(folder: true, name: 'rootfolder', children: [subFolder, file02])
        def fetcher = new AbstractFileFetcher({ it.folder }, { it.name }, { it.children })

        when:
        def files = fetcher.iterate(rootFolder)

        then:
        files == ['rootfolder/subfolder/testfile01.txt': file01, 'rootfolder/testfile02.txt': file02]
    }
}
