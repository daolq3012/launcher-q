package seoft.co.kr.launcherq.ui.opensource


import android.arch.lifecycle.MutableLiveData
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.Opensource
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper

class OpensourceViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "OpensourceViewModel#$#"
    var livedOpensources = MutableLiveData<List<Opensource>>()

    val opensources = mutableListOf<Opensource>()

    override fun start() {
        opensources.add(Opensource(
            title =
            "ArthurHub/Android-Image-Cropper",
            content =
            "Originally forked from edmodo/cropper.\n" +
                    "\n" +
                    "Copyright 2016, Arthur Teplitzki, 2013, Edmodo, Inc.\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:\n" +
                    "\n" +
                    "http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License."
        ))

        opensources.add(
            Opensource(
            title =
            "square/picasso",
            content =
            "Copyright 2013 Square, Inc.\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."        )
        )

        opensources.add(Opensource(
            title =
            "hdodenhof/CircleImageView",
            content =
            "Copyright 2014 - 2018 Henning Dodenhof\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."        ))

        opensources.add(Opensource(
            title =
            "Rxjava2",
            content =
            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."        ))

        opensources.add(Opensource(
            title =
            "retrofit",
            content =
            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."        ))

        opensources.add(
            Opensource(
                title =
                "gson",
                content =
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."        )
        )

        livedOpensources.value = opensources
    }

    fun clickBackBt(){
        toActMsg(MsgType.FINISH_ACTIVITY)
    }


}