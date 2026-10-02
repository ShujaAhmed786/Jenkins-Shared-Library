def call(Map config = [:]) {
    def odcTool = config.get('odcInstallation', 'DP-Check')
    def target  = config.get('target', 'package.json')

    stage('OWASP Dependency-Check') {
        dependencyCheck additionalArguments: "--scan ${target} --format ALL", odcInstallation: odcTool
        dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
    }
}
