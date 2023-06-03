package gq97a6.pjatk.app.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import gq97a6.pjatk.app.BuildConfig
import gq97a6.pjatk.app.compose.*
import gq97a6.pjatk.app.objects.G.settings
import gq97a6.pjatk.app.objects.Storage.saveToFile

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeConstruct(requireContext()) {
        Box(contentAlignment = Alignment.BottomCenter) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Settings", fontSize = 45.sp, color = pallet.accent)

                FrameBox("Optionals") {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            var weeks by remember { mutableStateOf(settings.weeks.toString()) }
                            EditText(
                                label = { Text("Fetch period in weeks") },
                                placeholder = { Text("weeks") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(200.dp),
                                value = weeks,
                                enabled = false,
                                onValueChange = {})

                            BasicButton(
                                onClick = {
                                    settings.weeks = maxOf(minOf(settings.weeks + 1, 6), 1)
                                    settings.saveToFile()
                                    weeks = settings.weeks.toString()
                                },
                                contentPadding = PaddingValues(1.dp),
                                border = BorderStroke(1.dp, Colors.primary),
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp)
                            ) {
                                Text("+", fontSize = 20.sp, color = Colors.primary)
                            }

                            BasicButton(
                                onClick = {
                                    settings.weeks = maxOf(minOf(settings.weeks - 1, 6), 1)
                                    settings.saveToFile()
                                    weeks = settings.weeks.toString()
                                },
                                contentPadding = PaddingValues(1.dp),
                                border = BorderStroke(1.dp, Colors.primary),
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp)
                            ) {
                                Text("-", fontSize = 20.sp, color = Colors.primary)
                            }
                        }

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
            }

            Text(
                BuildConfig.VERSION_NAME,
                Modifier.padding(bottom = 5.dp),
                fontSize = 10.sp,
                color = pallet.accent.copy(0.2f)
            )
        }
    }
}