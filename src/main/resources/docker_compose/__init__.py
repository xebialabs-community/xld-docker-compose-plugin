#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

import sys

from overtherepy import OverthereHostSession


def execute_docker_compose(context, composed, application, command, options=''):
    session = OverthereHostSession(composed.container.host, stream_command_output=True, execution_context=context)
    runtime = {'application': application,
               'command': command,
               'options': options,
               'uploaded_compose_file': session.upload_file_to_work_dir(composed.file),
               'dockerHost': composed.container.dockerHost,
               'cert_pem': session.upload_text_content_to_work_dir(composed.container.certPem, "cert.pem"),
               'ca_pem': session.upload_text_content_to_work_dir(composed.container.caPem, "ca.pem"),
               'key_pem': session.upload_text_content_to_work_dir(composed.container.keyPem, "key.pem")}

    print "docker-compose {0}....".format(runtime['command'])
    command_line = "docker-compose --file {r[uploaded_compose_file].path} --project-name {r[application]} --tlscacert {r[ca_pem].path} --tlscert {r[cert_pem].path} --tlskey {r[key_pem].path} --tlsverify -H {r[dockerHost]} --skip-hostname-check {r[command]} {r[options]} ".format(
        r=runtime)

    print '---------------------------'
    print command_line
    print '---------------------------'

    response = session.execute(command_line)

    if response.rc != 0:
        sys.exit(response.rc)
