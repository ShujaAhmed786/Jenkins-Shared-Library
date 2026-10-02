def call(Map config = [:]) {
    def imageName = config.imageName
    def imageTag  = config.imageTag
    def severity  = config.get('severity', 'HIGH,CRITICAL')

    stage('Docker Build') {
        sh "docker build -t ${imageName}:${imageTag} -t ${imageName}:latest ."
    }

    stage('Trivy Container Scan') {
        sh "trivy image --severity ${severity} --format table ${imageName}:${imageTag}"
    }
}
