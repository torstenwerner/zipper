package xyz.its_me

import org.apache.commons.io.IOUtils
import spock.lang.Specification

class ZipCollectorTest extends Specification {
    def 'simple compression test'() {
        given:
        def inputStream = IOUtils.toInputStream('Hello Zipper!')
        def outputStream = new FileOutputStream('build/simple.zip')
        def collector = new ZipCollector(outputStream)

        when:
        collector.add('dir01/dir02/file.txt', inputStream)
        collector.close()

        then:
        def bytes = IOUtils.toByteArray(new FileInputStream('build/simple.zip'))
        bytes.length == 169
    }
}
