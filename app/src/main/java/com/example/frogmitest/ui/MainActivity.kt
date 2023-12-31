package com.example.frogmitest.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frogmitest.R
import com.example.frogmitest.core.Store
import com.example.frogmitest.ui.theme.FrogmiTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrogmiTestTheme {
                Log.d("Leo","FrogmiTestTheme")
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxSize()
                ) {
                    RenderAppStates(viewModel, this)
                }
            }
        }
    }

}

@Composable
fun RenderAppStates(viewModel: MainViewModel, context: Context) {
    val storeList: List<Store> by viewModel
        .storeList
        .observeAsState(listOf())

    Box(modifier = Modifier.fillMaxSize()) {
        ShowingList(list = storeList, viewModel)

        when (val state = viewModel.composeUiState) {
            is UiState.Success -> {}

            is UiState.Loading -> {
                val visible = (state).isVisible
                if (visible) {
                    Loader(Modifier.align(Alignment.Center))
                }
            }

            UiState.Error.ConnectionError -> {
                ErrorMessage(
                    modifier = Modifier.align(Alignment.Center),
                    stringResource(id = R.string.connection_error_message)
                )
            }

            UiState.Error.BadRequestError -> {
                ErrorMessage(
                    modifier = Modifier.align(Alignment.Center),
                    stringResource(id = R.string.bad_request_error_message)
                )
            }

            UiState.Error.ServerError -> {
                ErrorMessage(
                    modifier = Modifier.align(Alignment.Center),
                    stringResource(id = R.string.server_error_message)
                )
            }

            UiState.ReachLimit -> {
                Toast.makeText(context,R.string.limit_reached_message,Toast.LENGTH_LONG).show()
            }

        }
    }
}

@Composable
fun Loader(modifier: Modifier) {
    CircularProgressIndicator(
        modifier.size(100.dp),
        color = colorResource(id = R.color.purple_500),
        strokeWidth = 5.dp
    )
}

@Composable
fun ShowingList(list: List<Store>?, viewModel: MainViewModel) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(list!!) { store -> ListItem(store = store) }
        item {
            LaunchedEffect(false) {
                viewModel.getStores()
            }
        }
    }
}

@Composable
fun ListItem(store: Store, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        with(store) {
            Text(
                text = "$name | $code",
                modifier = modifier
                    .wrapContentWidth()
                    .align(Alignment.TopStart)
                    .padding(start = 5.dp),
                color = Color.Black,
                fontSize = 20.sp
            )
            Text(
                text = fullAddress,
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 5.dp),
                color = Color.Gray,
                fontSize = 15.sp
            )
        }
        Divider(
            thickness = 1.dp,
            color = Color.Blue,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
fun ErrorMessage(modifier: Modifier, errorMessage: String) {
    Surface(
        modifier = modifier.width(150.dp),
        color = colorResource(id = R.color.light_red),
        contentColor = Color.Red,
        border = BorderStroke(2.dp, Color.Red),
        shadowElevation = 5.dp
    ) {
        Text(
            text = errorMessage,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}